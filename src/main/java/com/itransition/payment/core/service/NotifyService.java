package com.itransition.payment.core.service;

import com.itransition.payment.core.dto.TransactionReplenishDto;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

public interface NotifyService {

    Mono<ResponseEntity<Void>> sendTransaction(TransactionReplenishDto replenishDto);
}
