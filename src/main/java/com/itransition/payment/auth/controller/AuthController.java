package com.itransition.payment.auth.controller;

import com.itransition.payment.auth.dto.LoginRequest;
import com.itransition.payment.auth.dto.LoginResponse;
import com.itransition.payment.auth.dto.RefreshTokenRequest;
import com.itransition.payment.auth.dto.RefreshTokenResponse;
import com.itransition.payment.auth.dto.LogoutRequest;
import com.itransition.payment.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/refresh")
    public RefreshTokenResponse refreshToken(@RequestBody RefreshTokenRequest request) {
        return authService.refreshToken(request);
    }

    @PostMapping("/logout")
    public void logout(@RequestBody LogoutRequest logoutRequest) {
        authService.logout(logoutRequest);
    }
}
