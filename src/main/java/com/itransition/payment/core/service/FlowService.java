package com.itransition.payment.core.service;

import com.itransition.payment.core.dto.TransactionAdapterStateDto;
import com.itransition.payment.core.dto.TransactionInfoDto;

import java.util.List;

public interface FlowService {

    TransactionInfoDto createTransaction(TransactionAdapterStateDto adapterStateDto);

    TransactionInfoDto updateTransaction(TransactionInfoDto infoDto);

    List<TransactionInfoDto> searchTransactions(String externalId, String provider);
}
