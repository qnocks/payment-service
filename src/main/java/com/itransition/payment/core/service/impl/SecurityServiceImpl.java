package com.itransition.payment.core.service.impl;

import com.itransition.payment.core.dto.AuthResponse;
import com.itransition.payment.core.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private AuthResponse currentAuthorization;
    private final WebClient webClient;

    @Override
    public AuthResponse authorize(String grantType, String clientSecret, String clientId) {
        if (isTokenExpired()) {
            currentAuthorization = processAuthorization(grantType, clientSecret, clientId);
            return currentAuthorization;
        }

        return currentAuthorization;
    }

    private boolean isTokenExpired() {
        if (currentAuthorization == null) {
            return true;
        }

        // TODO: implement real token validation when Core Service will issue real tokens
        return currentAuthorization.getExpiresIn() < 5000L;
    }

    private AuthResponse processAuthorization(String grantType, String clientSecret, String clientId) {
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
