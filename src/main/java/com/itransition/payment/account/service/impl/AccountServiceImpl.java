package com.itransition.payment.account.service.impl;

import com.itransition.payment.account.dto.AccountDto;
import com.itransition.payment.account.service.AccountService;
import com.itransition.payment.core.exception.ExceptionHelper;
import com.itransition.payment.security.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

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

    // TODO: move error handling logic to exception helper
    private AccountDto retrieveById(String id, String authHeader) {
        return webClient.get()
                .uri("account/{account_id}", id)
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .retrieve()
                .bodyToMono(AccountDto.class)
                .onErrorMap(throwable -> exceptionHelper.handleExternalException(throwable, AccountService.class, id))
                .block();
    }
}
