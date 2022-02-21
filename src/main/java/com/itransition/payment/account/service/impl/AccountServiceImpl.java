package com.itransition.payment.account.service.impl;

import com.itransition.payment.account.dto.AccountDto;
import com.itransition.payment.account.service.AccountService;
import com.itransition.payment.core.exception.ExceptionMessageResolver;
import com.itransition.payment.core.exception.custom.AccountException;
import com.itransition.payment.security.service.SecurityService;
import lombok.RequiredArgsConstructor;
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
                .onStatus(HttpStatus::is4xxClientError,
                        response -> Mono.error(getAccountAbsenceException(id))
                )
                .bodyToMono(AccountDto.class)
                .block();

        if (accountDto == null) {
            throw getAccountAbsenceException(id);
        }

        return accountDto;
    }

    private AccountException getAccountAbsenceException(String id) {
        return AccountException.builder()
                .message(exceptionMessageResolver.getMessage("account.cannot-get", id))
                .build();
    }
}
