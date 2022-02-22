package com.itransition.payment.security.service.impl;

import com.itransition.payment.core.exception.ExceptionHelper;
import com.itransition.payment.security.dto.AuthResponse;
import com.itransition.payment.security.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    // TODO: Should be changed to config external file when implement real auth flow in Core Service
    private final String grantType = "";
    private final String clientSecret = "";
    private final String clientId = "";

    private AuthResponse currentAuthorization;
    private final WebClient webClient;
    private final ExceptionHelper exceptionHelper;

    @Override
    public String getAuthHeader() {
        AuthResponse authResponse = authorize();
        return authResponse.getTokenType() + " " + authResponse.getAccessToken();
    }

    private AuthResponse authorize() {
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
        AuthResponse response = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/auth/token")
                        .queryParam("grant_type", grantType)
                        .queryParam("client_secret", clientSecret)
                        .queryParam("client_id", clientId)
                        .build())
                .retrieve()
                .onStatus(
                        HttpStatus::is5xxServerError,
                        clientResponse -> Mono.error(exceptionHelper.buildExternalException("security.auth-error")))
                .bodyToMono(AuthResponse.class)
                .block();

        if (response == null) {
            throw new IllegalStateException();
        }

        return response;
    }
}
