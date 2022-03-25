package com.itransition.payment.auth.service.impl;

import com.itransition.payment.auth.crypto.Encoder;
import com.itransition.payment.auth.dto.LoginRequest;
import com.itransition.payment.auth.dto.LoginResponse;
import com.itransition.payment.auth.dto.RefreshTokenRequest;
import com.itransition.payment.auth.dto.RefreshTokenResponse;
import com.itransition.payment.auth.entity.RefreshToken;
import com.itransition.payment.auth.dto.LogoutRequest;
import com.itransition.payment.auth.entity.User;
import com.itransition.payment.auth.repository.UserRepository;
import com.itransition.payment.auth.security.crypto.CredentialsEncoder;
import com.itransition.payment.auth.security.jwt.JwtTokenProvider;
import com.itransition.payment.auth.service.AuthService;
import com.itransition.payment.auth.service.RefreshTokenService;
import com.itransition.payment.auth.service.SessionService;
import com.itransition.payment.core.exception.ExceptionHelper;
import com.itransition.payment.core.exception.ExceptionMessageResolver;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final SessionService sessionService;
    private final RefreshTokenService refreshTokenService;
    private final CredentialsEncoder credentialsEncoder;
    private final ExceptionHelper exceptionHelper;
    private final ExceptionMessageResolver exceptionMessageResolver;
    private final Encoder encoder;

    @Override
    public LoginResponse login(@NotNull LoginRequest loginRequest) {
        val username = loginRequest.getUsername();
        val encodedUsername = credentialsEncoder.encode(username);

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                encodedUsername, loginRequest.getPassword()));

        val user = getUserByUsername(encodedUsername);
        verifySessionAbsence(user, loginRequest);
        val tokenPayload = jwtTokenProvider.createToken(encodedUsername, user.getRoles());
        sessionService.createOrUpdate(user, tokenPayload);

        return LoginResponse.builder()
                .username(username)
                .accessToken(tokenPayload.getToken())
                .type(JwtTokenProvider.getTokenType())
                .refreshToken(refreshTokenService.createRefreshToken(user).getToken())
                .build();
    }

    @Override
    public RefreshTokenResponse refreshToken(@NotNull RefreshTokenRequest request) {
        val refreshToken = refreshTokenService.getByToken(request.getRefreshToken());
        verifyRefreshTokenValidation(refreshToken);
        return processRefreshing(refreshToken);
    }

    @Override
    public void logout(@NotNull LogoutRequest logoutRequest) {
        val user = getUserByUsername(encoder.encode(logoutRequest.getUsername()));
        sessionService.removeByUserId(user.getId());
    }

    private void verifySessionAbsence(@NotNull User user, @NotNull LoginRequest loginRequest) {
        val isExists = sessionService.existsByUserId(user.getId());
        if (isExists) {
            throw exceptionHelper.buildAuthException(
                    HttpStatus.CONFLICT, "auth.session.exists", loginRequest.getUsername());
        }
    }

    private void verifyRefreshTokenValidation(@NotNull RefreshToken refreshToken) {
        val isTokenNotValid = !refreshTokenService.verifyAndDeleteExpired(refreshToken);

        if (isTokenNotValid) {
            throw exceptionHelper.buildAuthException(
                    HttpStatus.BAD_REQUEST, "auth.token.refresh.expired", refreshToken.getToken());
        }
    }

    private RefreshTokenResponse processRefreshing(@NotNull RefreshToken refreshToken) {
        val accessToken = jwtTokenProvider.createToken(
                refreshToken.getUser().getUsername(), refreshToken.getUser().getRoles());

        refreshTokenService.resetExpiration(refreshToken);
        sessionService.createOrUpdate(refreshToken.getUser(), accessToken);

        return RefreshTokenResponse.builder()
                .accessToken(accessToken.getToken())
                .type(JwtTokenProvider.getTokenType())
                .refreshToken(refreshToken.getToken())
                .build();
    }

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(exceptionMessageResolver.getMessage(
                        "auth.username-not-found", username)));
    }
}
