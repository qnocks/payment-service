package com.itransition.payment.auth.controller;

import com.itransition.payment.auth.dto.LoginRequest;
import com.itransition.payment.auth.dto.LoginResponse;
import com.itransition.payment.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public LoginResponse admin() {
        return LoginResponse.builder()
                .username("admin page")
                .token("admin page")
                .build();
    }

    @GetMapping("/user")
    public LoginResponse user() {
        return LoginResponse.builder()
                .username("user page")
                .token("user page")
                .build();
    }
}
