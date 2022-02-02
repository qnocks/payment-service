package com.itransition.payment.core.service.impl;

import com.itransition.payment.core.dto.TransactionAdapterStateDto;
import com.itransition.payment.core.dto.TransactionInfoDto;
import com.itransition.payment.core.service.AccountService;
import com.itransition.payment.core.service.FlowService;
import com.itransition.payment.core.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FlowServiceImpl implements FlowService {

    private final WebClient webClient;

    private final TransactionService transactionService;

    private final AccountService accountService;

    @Override
    public TransactionInfoDto createTransaction(TransactionAdapterStateDto transactionAdapterStateDto) {

        // verify external id uniqueness

        // auth

        // verify account existence

        // persist transaction

        return null;
    }

    @Override
    public TransactionInfoDto updateTransaction(TransactionInfoDto transactionInfoDto) {

        // verify correctness of status transaction

        // update transaction

        return null;
    }

    @Override
    public List<TransactionInfoDto> searchTransaction(String externalId, String provider) {

        // get transaction by criteria

        return null;
    }
}
