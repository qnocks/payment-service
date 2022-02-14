package com.itransition.payment.core.service;

import com.itransition.payment.core.dto.TransactionStateDto;
import com.itransition.payment.core.dto.TransactionInfoDto;

import java.util.List;

public interface FlowService {

    TransactionInfoDto createTransaction(TransactionStateDto stateDto);

    TransactionInfoDto updateTransaction(TransactionInfoDto updateDto);

    List<TransactionInfoDto> searchTransactions(String externalId, String provider);
}
