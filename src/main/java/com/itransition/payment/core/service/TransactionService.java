package com.itransition.payment.core.service;

import com.itransition.payment.core.dto.TransactionInfoDto;

import java.util.Optional;

public interface TransactionService {

    Optional<TransactionInfoDto> findByExternalId(String externalId);
}
