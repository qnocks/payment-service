package com.itransition.payment.core.service;

import com.itransition.payment.core.dto.TransactionAdapterStateDto;
import com.itransition.payment.core.dto.TransactionInfoDto;

import java.util.List;

public interface FlowService {

    TransactionInfoDto createTransaction(TransactionAdapterStateDto transactionAdapterStateDto);

    TransactionInfoDto updateTransaction(TransactionInfoDto transactionInfoDto);

    List<TransactionInfoDto> searchTransaction(String externalId, String provider);
}
