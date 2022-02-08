package com.itransition.payment.core.service.impl;

import com.itransition.payment.core.domain.enums.TransactionStatus;
import com.itransition.payment.core.dto.AccountDto;
import com.itransition.payment.core.dto.TransactionAdapterStateDto;
import com.itransition.payment.core.dto.TransactionInfoDto;
import com.itransition.payment.core.exception.ExceptionMessageResolver;
import com.itransition.payment.core.service.AccountService;
import com.itransition.payment.core.service.FlowService;
import com.itransition.payment.core.service.TransactionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FlowServiceImpl implements FlowService {

    private final TransactionService transactionService;
    private final AccountService accountService;
    private final ExceptionMessageResolver exceptionMessageResolver;

    @Override
    public TransactionInfoDto createTransaction(TransactionAdapterStateDto transactionAdapterStateDto) {
        verifyExternalIdUniqueness(transactionAdapterStateDto.getExternalId());
        verifyAccountExistence(transactionAdapterStateDto.getUser());
        return transactionService.save(transactionAdapterStateDto);
    }

    private void verifyExternalIdUniqueness(String externalId) {
        boolean isTransactionExists = transactionService.existsByExternalId(externalId);

        // TODO: Should be changed to custom exception when implementation of exception handling
        if (isTransactionExists) {
            throw new IllegalStateException(
                    exceptionMessageResolver.getMessage("flow.external-id-non-uniqueness", externalId));
        }
    }

    private void verifyAccountExistence(String userId) {
        AccountDto accountDto = accountService.getById(userId);

        // TODO: Should be changed to custom exception when implementation of exception handling
        if (accountDto == null) {
            throw new IllegalStateException(exceptionMessageResolver.getMessage("flow.account-absence", userId));
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

        // TODO: Should be changed to custom exception when implementation of exception handling
        if (!TransactionStatus.INITIAL.equals(status)) {
            throw new IllegalStateException(
                    exceptionMessageResolver.getMessage("flow.transaction-status-incorrectness", status));
        }
    }

    @Override
    public List<TransactionInfoDto> searchTransactions(String externalId, String provider) {
        return transactionService.getAllByExternalIdOrProvider(externalId, provider);
    }
}
