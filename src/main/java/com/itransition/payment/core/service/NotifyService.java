package com.itransition.payment.core.service;

import com.itransition.payment.core.dto.TransactionReplenishDto;
import com.itransition.payment.core.dto.ReplenishResponse;
import reactor.core.publisher.Mono;

public interface NotifyService {

    Mono<ReplenishResponse> sendTransaction(TransactionReplenishDto replenishDto);
}
