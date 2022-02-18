package com.itransition.payment.flow.service.impl;

import com.itransition.payment.core.exception.custom.AccountAbsenceException;
import com.itransition.payment.core.exception.custom.TransactionNotUniqueException;
import com.itransition.payment.core.exception.custom.TransactionStatusCannotBeChangedException;
import com.itransition.payment.core.types.TransactionStatus;
import com.itransition.payment.account.dto.AccountDto;
import com.itransition.payment.core.dto.TransactionStateDto;
import com.itransition.payment.core.dto.TransactionInfoDto;
import com.itransition.payment.core.exception.ExceptionMessageResolver;
import com.itransition.payment.account.service.AccountService;
import com.itransition.payment.flow.service.FlowService;
import com.itransition.payment.transaction.service.TransactionService;
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
    public TransactionInfoDto createTransaction(TransactionStateDto stateDto) {
        verifyForUnique(stateDto.getExternalId(), stateDto.getProvider());
        verifyAccountExistence(stateDto.getUser());
        return transactionService.save(stateDto);
    }

    private void verifyForUnique(String externalId, String providerName) {
        boolean isTransactionExists = transactionService.existsByExternalIdAndProvider(externalId, providerName);

        if (isTransactionExists) {
            throw new TransactionNotUniqueException(exceptionMessageResolver.getMessage(
                    "flow.external-id-provider-non-uniqueness", externalId, providerName));
        }
    }

    private void verifyAccountExistence(String userId) {
        AccountDto accountDto = accountService.getById(userId);

        if (accountDto == null) {
            throw new AccountAbsenceException(exceptionMessageResolver.getMessage("flow.account-absence", userId));
        }
    }

    @Override
    public TransactionInfoDto updateTransaction(TransactionInfoDto updateDto) {
        verifyStatusTransactionCorrectness(updateDto.getExternalId(), updateDto.getProvider());
        return transactionService.update(updateDto);
    }

    private void verifyStatusTransactionCorrectness(String externalId, String providerName) {
        var existingTransaction = transactionService.getByExternalIdAndProvider(externalId, providerName);
        var status = existingTransaction.getStatus();

        if (!TransactionStatus.INITIAL.equals(status)) {
            throw new TransactionStatusCannotBeChangedException(exceptionMessageResolver.getMessage(
                    "flow.transaction-status-incorrectness", externalId, providerName, status));
        }
    }

    @Override
    public List<TransactionInfoDto> searchTransactions(String externalId, String provider) {
        return List.of(transactionService.getByExternalIdAndProvider(externalId, provider));
    }
}
