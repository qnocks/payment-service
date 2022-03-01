package com.itransition.payment.security.service.impl;

import com.itransition.payment.core.exception.ExceptionHelper;
import com.itransition.payment.core.exception.custom.ExternalException;
import com.itransition.payment.security.dto.AuthResponse;
import com.itransition.payment.security.service.SecurityService;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;

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
                .bodyToMono(AuthResponse.class)
                .onErrorMap(this::handleError)
                .blockOptional()
                .orElseThrow(() -> exceptionHelper.buildExternalException("security.auth-error"));
    }

    private String getToken(@NotNull AuthResponse authResponse) {
        return authResponse.getTokenType() + " " + authResponse.getAccessToken();
    }

    private ExternalException handleError(Throwable e) {
        if (e instanceof WebClientRequestException) {
            throw exceptionHelper.buildExternalException("security.service-not-available");
        }

        throw exceptionHelper.buildExternalException("security.auth-error");
    }
}
