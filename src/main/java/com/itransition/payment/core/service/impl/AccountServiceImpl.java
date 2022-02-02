package com.itransition.payment.core.service.impl;

import com.itransition.payment.core.dto.AccountDto;
import com.itransition.payment.core.dto.AuthResponse;
import com.itransition.payment.core.service.AccountService;
import com.itransition.payment.core.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final WebClient webClient;

    private final SecurityService securityService;

    @Override
    public AccountDto getById(String id) {
        String authHeader = getAuthHeader();
        return retrieveById(id, authHeader);
    }

    private String getAuthHeader() {
        AuthResponse authResponse = securityService.authorize("", "", "");
        return authResponse.getTokenType() + " " + authResponse.getAccessToken();
    }

    private AccountDto retrieveById(String id, String authHeader) {
        AccountDto accountDto = webClient.get()
                .uri("account/" + id)
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .retrieve()
                .bodyToMono(AccountDto.class)
                .block();

        if (accountDto == null) {
            String msg = String.format("Cannot get Account with id: %s", id);
            log.warn(msg);

            // Should be changed to custom exception when implementation of exception handling
            throw new IllegalStateException(msg);
        }

        return accountDto;
    }
}
