package com.itransition.payment.core.service.impl;

import com.itransition.payment.core.dto.AuthResponse;
import com.itransition.payment.core.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final WebClient webClient;

    @Override
    public AuthResponse authorize(String grantType, String clientSecret, String clientId) {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/auth/token")
                        .queryParam("grant_type", grantType)
                        .queryParam("client_secret", clientSecret)
                        .queryParam("client_id", clientId)
                        .build()
                ).retrieve()
                .bodyToMono(AuthResponse.class)
                .block();
    }
}
