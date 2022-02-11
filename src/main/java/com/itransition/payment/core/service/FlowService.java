package com.itransition.payment.core.service;

import com.itransition.payment.core.dto.TransactionAdapterStateDto;
import com.itransition.payment.core.dto.TransactionInfoDto;

import com.itransition.payment.core.dto.TransactionUpdateDto;
import java.util.List;

public interface FlowService {

    TransactionInfoDto createTransaction(TransactionAdapterStateDto adapterStateDto);

    TransactionInfoDto updateTransaction(TransactionUpdateDto updateDto);

    List<TransactionInfoDto> searchTransactions(String externalId, String provider);
}
