package com.itransition.payment.core.service.impl;

import com.itransition.payment.core.domain.enums.TransactionStatus;
import com.itransition.payment.core.dto.AccountDto;
import com.itransition.payment.core.dto.TransactionAdapterStateDto;
import com.itransition.payment.core.dto.TransactionInfoDto;
import com.itransition.payment.core.service.AccountService;
import com.itransition.payment.core.service.FlowService;
import com.itransition.payment.core.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlowServiceImpl implements FlowService {

    private final TransactionService transactionService;

    private final AccountService accountService;

    @Override
    public TransactionInfoDto createTransaction(TransactionAdapterStateDto transactionAdapterStateDto) {
        verifyExternalIdUniqueness(transactionAdapterStateDto.getExternalId());
        verifyAccountExistence(transactionAdapterStateDto.getUser());

        return transactionService.save(transactionAdapterStateDto);
    }

    private void verifyExternalIdUniqueness(String externalId) {
        boolean exists = transactionService.existsByExternalId(externalId);

        // Should be changed to custom exception when implementation of exception handling
        if (exists) {
            String msg = String.format("External id: %s isn't unique", externalId);

            log.warn(msg);
            throw new IllegalStateException();
        }
    }

    private void verifyAccountExistence(String userId) {
        AccountDto accountDto = accountService.getById(Long.parseLong(userId));

        // Should be changed to custom exception when implementation of exception handling
        if (accountDto == null) {
            String msg = String.format("Cannot find account with id: %s", userId);

            log.warn(msg);
            throw new IllegalStateException();
        }
    }

    @Override
    public TransactionInfoDto updateTransaction(TransactionInfoDto transactionInfoDto) {
        verifyStatusTransactionCorrectness(transactionInfoDto.getExternalId());
        return transactionService.update(transactionInfoDto);
    }

    private void verifyStatusTransactionCorrectness(String externalId) {
        TransactionInfoDto existingTransaction = transactionService.getByExternalId(externalId);
        TransactionStatus status = existingTransaction.getStatus();

        // Should be changed to custom exception when implementation of exception handling
        if (!status.equals(TransactionStatus.INITIAL)) {
            String msg = String.format(
                    "Cannot change transaction status. The transaction has %s status", status.getName());

            log.warn(msg);
            throw new IllegalStateException(msg);
        }
    }

    @Override
    public List<TransactionInfoDto> searchTransactions(String externalId, String provider) {
        return transactionService.getAllByExternalIdOrProvider(externalId, provider);
    }
}
