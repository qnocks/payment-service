package com.itransition.payment.flow.service.impl;

import com.itransition.payment.account.service.AccountService;
import com.itransition.payment.core.dto.TransactionInfoDto;
import com.itransition.payment.core.dto.TransactionStateDto;
import com.itransition.payment.core.exception.ExceptionHelper;
import com.itransition.payment.core.type.TransactionStatus;
import com.itransition.payment.flow.service.FlowService;
import com.itransition.payment.transaction.service.TransactionService;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FlowServiceImpl implements FlowService {

    private final TransactionService transactionService;
    private final AccountService accountService;
    private final ExceptionHelper exceptionHelper;

    @Override
    public TransactionInfoDto createTransaction(@NotNull TransactionStateDto stateDto) {
        verifyForUnique(stateDto.getExternalId(), stateDto.getProvider());
        verifyAccountExistence(stateDto.getUser());
        return transactionService.save(stateDto);
    }

    @Override
    public TransactionInfoDto updateTransaction(@NotNull TransactionInfoDto updateDto) {
        verifyStatusTransactionCorrectness(updateDto.getExternalId(), updateDto.getProvider());
        return transactionService.update(updateDto);
    }

    @Override
    public List<TransactionInfoDto> searchTransactions(String externalId, String provider) {
        return List.of(transactionService.getByExternalIdAndProvider(externalId, provider));
    }

    private void verifyForUnique(String externalId, String providerName) {
        val isExists = transactionService.existsByExternalIdAndProvider(externalId, providerName);
        if (isExists) {
            throw exceptionHelper.buildTransactionException(
                    "flow.external-id-provider-non-uniqueness", externalId, providerName);
        }
    }

    private void verifyAccountExistence(String userId) {
        accountService.getById(userId);
    }

    private void verifyStatusTransactionCorrectness(String externalId, String providerName) {
        val status = transactionService.getByExternalIdAndProvider(externalId, providerName).getStatus();

        if (!TransactionStatus.INITIAL.equals(status)) {
            throw exceptionHelper.buildTransactionException(
                    "flow.transaction-status-incorrectness", externalId, providerName, status);
        }
    }
}
