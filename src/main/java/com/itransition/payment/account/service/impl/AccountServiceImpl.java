package com.itransition.payment.account.service.impl;

import com.itransition.payment.account.dto.AccountDto;
import com.itransition.payment.account.service.AccountService;
import com.itransition.payment.core.exception.ExceptionHelper;
import com.itransition.payment.security.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final WebClient webClient;
    private final SecurityService securityService;
    private final ExceptionHelper exceptionHelper;

    @Override
    public AccountDto getById(String id) {
        val authHeader = securityService.getAuthHeader();
        return retrieveById(id, authHeader);
    }

    private AccountDto retrieveById(String id, String authHeader) {
        return webClient.get()
                .uri("account/" + id)
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .retrieve()
                .onStatus(HttpStatus::isError, response -> Mono.error(exceptionHelper.buildExternalException(
                        HttpStatus.BAD_REQUEST, "account.cannot-get", id)))
                .bodyToMono(AccountDto.class)
                .onErrorResume(
                        error -> Mono.error(exceptionHelper.buildExternalException("account.service-not-available")))
                .block();
    }
}
