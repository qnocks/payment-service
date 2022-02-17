package com.itransition.payment.account.service.impl;

import com.itransition.payment.account.dto.AccountDto;
import com.itransition.payment.security.dto.AuthResponse;
import com.itransition.payment.core.exception.ExceptionMessageResolver;
import com.itransition.payment.account.service.AccountService;
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
    private final ExceptionMessageResolver exceptionMessageResolver;

    @Override
    public AccountDto getById(String id) {
        String authHeader = securityService.getAuthHeader();
        return retrieveById(id, authHeader);
    }

    private AccountDto retrieveById(String id, String authHeader) {
        AccountDto accountDto = webClient.get()
                .uri("account/" + id)
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .retrieve()
                .bodyToMono(AccountDto.class)
                .block();

        if (accountDto == null) {
            // TODO: Should be changed to custom exception when implementation of exception handling
            throw new IllegalStateException(exceptionMessageResolver.getMessage("account.cannot-get", id));
        }

        return accountDto;
    }
}
