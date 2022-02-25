package com.itransition.payment.security.service.impl;

import com.itransition.payment.core.exception.ExceptionHelper;
import com.itransition.payment.security.dto.AuthResponse;
import com.itransition.payment.security.service.SecurityService;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    // TODO: Should be changed to config external file when implement real auth flow in Core Service
    private static final String GRANT_TYPE = "";
    private static final String CLIENT_SECRET = "";
    private static final String CLIENT_ID = "";

    private AuthResponse currentAuthorization;
    private final WebClient webClient;
    private final ExceptionHelper exceptionHelper;

    @Override
    public String getAuthHeader() {
        val authResponse = authorize();
        return getToken(authResponse);
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
        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/auth/token")
                        .queryParam("grant_type", GRANT_TYPE)
                        .queryParam("client_secret", CLIENT_SECRET)
                        .queryParam("client_id", CLIENT_ID)
                        .build())
                .retrieve()
                .onStatus(
                        HttpStatus::isError,
                        response -> Mono.error(exceptionHelper.buildExternalException("security.auth-error")))
                .bodyToMono(AuthResponse.class)
                .onErrorResume(
                        error -> Mono.error(exceptionHelper.buildExternalException("security.service-not-available")))
                .blockOptional()
                .orElseThrow(() -> exceptionHelper.buildExternalException("security.auth-error"));
    }

    private String getToken(@NotNull AuthResponse authResponse) {
        return authResponse.getTokenType() + " " + authResponse.getAccessToken();
    }
}
