package com.itransition.payment.account.service.impl;

import com.itransition.payment.account.dto.AccountDto;
import com.itransition.payment.account.service.AccountService;
import com.itransition.payment.core.exception.ExceptionHelper;
import com.itransition.payment.core.exception.custom.ExternalException;
import com.itransition.payment.security.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final WebClient webClient;
    private final SecurityService securityService;
    private final ExceptionHelper exceptionHelper;

    @Override
    public AccountDto getById(String id) {
        return retrieveById(id, securityService.getAuthHeader());
    }

    private AccountDto retrieveById(String id, String authHeader) {
        return webClient.get()
                .uri("account/{account_id}", id)
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .retrieve()
                .bodyToMono(AccountDto.class)
                .onErrorMap(throwable -> handleError(throwable, id))
                .block();
    }

    private ExternalException handleError(Throwable e, String id) {
        if (e instanceof WebClientRequestException) {
            throw exceptionHelper.buildExternalException("account.service-not-available");
        }

        throw exceptionHelper.buildExternalException(HttpStatus.BAD_REQUEST, "account.cannot-get", id);
    }
}
