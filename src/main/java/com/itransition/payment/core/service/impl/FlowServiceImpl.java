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
    public TransactionInfoDto createTransaction(TransactionAdapterStateDto adapterStateDto) {
        verifyExternalIdAndProviderUniqueness(adapterStateDto.getExternalId(), adapterStateDto.getProvider());
        verifyAccountExistence(adapterStateDto.getUser());
        return transactionService.save(adapterStateDto);
    }

    private void verifyExternalIdAndProviderUniqueness(String externalId, String providerName) {
        boolean isTransactionExists = transactionService.existsByExternalIdAndProvider(externalId, providerName);

        // TODO: Should be changed to custom exception when implementation of exception handling
        if (isTransactionExists) {
            throw new IllegalStateException(
                    exceptionMessageResolver.getMessage("flow.external-id-provider-non-uniqueness", externalId));
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
        verifyStatusTransactionCorrectness(transactionInfoDto.getId());
        return transactionService.update(transactionInfoDto);
    }

    private void verifyStatusTransactionCorrectness(Long id) {
        TransactionInfoDto existingTransaction = transactionService.getById(id);
        TransactionStatus status = existingTransaction.getStatus();

        // TODO: Should be changed to custom exception when implementation of exception handling
        if (!TransactionStatus.INITIAL.equals(status)) {
            throw new IllegalStateException(
                    exceptionMessageResolver.getMessage("flow.transaction-status-incorrectness", id, status));
        }
    }

    @Override
    public List<TransactionInfoDto> searchTransactions(String externalId, String provider) {
        return List.of(transactionService.getByExternalIdAndProvider(externalId, provider));
    }
}
