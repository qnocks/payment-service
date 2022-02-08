package com.itransition.payment.core.service;

import com.itransition.payment.core.dto.TransactionAdapterStateDto;
import com.itransition.payment.core.dto.TransactionInfoDto;

import java.util.List;

public interface TransactionService {

    TransactionInfoDto save(TransactionAdapterStateDto transactionAdapterStateDto);

    TransactionInfoDto update(TransactionInfoDto transactionInfoDto);

    Boolean existsByExternalId(String externalId);

    TransactionInfoDto getByExternalId(String externalId);

    List<TransactionInfoDto> getAllByExternalIdOrProvider(String externalId, String provider);
}
