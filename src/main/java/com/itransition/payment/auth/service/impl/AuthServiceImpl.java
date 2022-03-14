package com.itransition.payment.auth.service.impl;

import com.itransition.payment.auth.dto.LoginRequest;
import com.itransition.payment.auth.dto.LoginResponse;
import com.itransition.payment.auth.repository.UserRepository;
import com.itransition.payment.auth.security.jwt.JwtTokenProvider;
import com.itransition.payment.auth.service.AuthService;
import com.itransition.payment.auth.service.SessionService;
import com.itransition.payment.core.exception.ExceptionMessageResolver;
import lombok.RequiredArgsConstructor;
import lombok.val;
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
    private final ExceptionMessageResolver exceptionMessageResolver;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        val username = loginRequest.getUsername();

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                username, loginRequest.getPassword()));

        val user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(
                exceptionMessageResolver.getMessage("auth.username-not-found", username)));
        val tokenPair = jwtTokenProvider.createToken(username, user.getRoles());

        sessionService.createSession(user, tokenPair);

        return LoginResponse.builder()
                .username(username)
                .token(tokenPair.getToken())
                .build();
    }
}
