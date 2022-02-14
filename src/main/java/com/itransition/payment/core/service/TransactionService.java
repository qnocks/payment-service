package com.itransition.payment.core.service;

import com.itransition.payment.core.dto.TransactionAdapterStateDto;
import com.itransition.payment.core.dto.TransactionInfoDto;

public interface TransactionService {

    TransactionInfoDto save(TransactionAdapterStateDto adapterStateDto);

    TransactionInfoDto update(TransactionInfoDto updateDto);

    boolean existsByExternalIdAndProvider(String externalId, String providerName);

    TransactionInfoDto getByExternalIdAndProvider(String externalId, String provider);
}
