package com.itransition.payment.auth.service.impl;

import com.itransition.payment.auth.dto.LoginRequest;
import com.itransition.payment.auth.dto.LoginResponse;
import com.itransition.payment.auth.repository.UserRepository;
import com.itransition.payment.auth.security.jwt.JwtTokenProvider;
import com.itransition.payment.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        val username = loginRequest.getUsername();

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                username, loginRequest.getPassword()));

        // TODO: add custom exception handling for auth flow
        val user = userRepository.findByUsername(username).orElseThrow(IllegalArgumentException::new);
        val token = jwtTokenProvider.createToken(username, user.getRoles());

        return LoginResponse.builder()
                .username(username)
                .token(token)
                .build();
    }
}
