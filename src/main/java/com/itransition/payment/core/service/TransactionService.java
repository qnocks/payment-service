package com.itransition.payment.core.service;

import com.itransition.payment.core.dto.TransactionAdapterStateDto;
import com.itransition.payment.core.dto.TransactionInfoDto;
import com.itransition.payment.core.dto.TransactionUpdateDto;

public interface TransactionService {

    TransactionInfoDto save(TransactionAdapterStateDto adapterStateDto);

    TransactionInfoDto update(TransactionUpdateDto updateDto);

    boolean existsByExternalIdAndProvider(String externalId, String providerName);

    TransactionInfoDto getByExternalIdAndProvider(String externalId, String provider);
}
