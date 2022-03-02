package com.itransition.payment.notify.service.impl;

import com.itransition.payment.core.dto.TransactionReplenishDto;
import com.itransition.payment.notify.service.NotifyService;
import com.itransition.payment.security.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class NotifyServiceImpl implements NotifyService {

    private final WebClient webClient;
    private final SecurityService securityService;

    @Override
    public Mono<ResponseEntity<Void>> sendTransaction(TransactionReplenishDto replenishDto) {
        return callExternalTransaction(securityService.getAuthHeader(), replenishDto);
    }

    private Mono<ResponseEntity<Void>> callExternalTransaction(String authHeader, TransactionReplenishDto replenishDto) {
        return webClient
                .post()
                .uri("/transaction")
                .header(HttpHeaders.AUTHORIZATION, authHeader)
                .body(Mono.just(replenishDto), TransactionReplenishDto.class)
                .retrieve()
                .toBodilessEntity();
    }
}
