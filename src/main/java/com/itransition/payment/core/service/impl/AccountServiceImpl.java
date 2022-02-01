package com.itransition.payment.core.service.impl;

import com.itransition.payment.core.dto.AccountDto;
import com.itransition.payment.core.dto.AuthResponse;
import com.itransition.payment.core.service.AccountService;
import com.itransition.payment.core.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final WebClient webClient;

    private final SecurityService securityService;

    @Override
    public AccountDto getById(Long id) {
        AuthResponse authResponse = securityService.authorize("", "", "");
        String header = authResponse.getTokenType() + " " + authResponse.getAccessToken();

        return webClient.get()
                .uri("account/" + id)
                .header(HttpHeaders.AUTHORIZATION, header)
                .retrieve()
                .bodyToMono(AccountDto.class)
                .block();
    }
}
