package com.itransition.payment.core.service.impl;

import com.itransition.payment.core.dto.AuthResponse;
import com.itransition.payment.core.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    // TODO: Should be changed to config external file when implement real auth flow in Core Service
    private final String GRANT_TYPE = "";
    private final String CLIENT_SECRET = "";
    private final String CLIENT_ID = "";

    private AuthResponse currentAuthorization;
    private final WebClient webClient;

    @Override
    public AuthResponse authorize() {
        if (isTokenExpired()) {
            currentAuthorization = processAuthorization();
            return currentAuthorization;
        }

        return currentAuthorization;
    }

    private boolean isTokenExpired() {
        if (currentAuthorization == null) {
            return true;
        }

        // TODO: implement real token validation when Core Service will issue real tokens
        return currentAuthorization.getExpiresIn() < 5000;
    }

    private AuthResponse processAuthorization() {
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/auth/token")
                        .queryParam("grant_type", GRANT_TYPE)
                        .queryParam("client_secret", CLIENT_SECRET)
                        .queryParam("client_id", CLIENT_ID)
                        .build()
                ).retrieve()
                .bodyToMono(AuthResponse.class)
                .block();
    }
}
