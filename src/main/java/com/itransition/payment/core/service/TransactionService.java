package com.itransition.payment.core.service;

import com.itransition.payment.core.dto.TransactionAdapterStateDto;
import com.itransition.payment.core.dto.TransactionInfoDto;

import java.util.List;

public interface TransactionService {

    TransactionInfoDto save(TransactionAdapterStateDto transactionAdapterStateDto);

    TransactionInfoDto update(TransactionInfoDto transactionInfoDto);

    boolean existsByExternalIdAndProvider(String externalId, String providerName);

    TransactionInfoDto getByExternalIdAndProvider(String externalId, String provider);

    TransactionInfoDto getById(Long id);
}
