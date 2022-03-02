package com.itransition.payment.unit.flow.service;

import com.itransition.payment.AssertionsHelper;
import com.itransition.payment.TestDataProvider;
import com.itransition.payment.account.dto.AccountDto;
import com.itransition.payment.account.service.AccountService;
import com.itransition.payment.core.dto.TransactionInfoDto;
import com.itransition.payment.core.exception.ExceptionHelper;
import com.itransition.payment.core.exception.custom.ExternalException;
import com.itransition.payment.core.exception.custom.TransactionException;
import com.itransition.payment.core.type.TransactionStatus;
import com.itransition.payment.flow.service.impl.FlowServiceImpl;
import com.itransition.payment.transaction.service.TransactionService;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FlowServiceTest {

    @InjectMocks
    private FlowServiceImpl underTest;

    @Mock
    private TransactionService transactionService;

    @Mock
    private AccountService accountService;

    @Mock
    private ExceptionHelper exceptionHelper;

    @Test
    void shouldCreateTransaction() {
        val stateDto = TestDataProvider.getTransactionStateDto();
        val expected = TestDataProvider.getTransactionInfoDto();

        when(transactionService.existsByExternalIdAndProvider(
                stateDto.getExternalId(), stateDto.getProvider())).thenReturn(false);
        when(accountService.getById(stateDto.getUser())).thenReturn(AccountDto.builder().build());
        when(transactionService.save(stateDto)).thenReturn(expected);

        val actual = underTest.createTransaction(stateDto);

        verify(transactionService, times(1)).save(stateDto);
        AssertionsHelper.verifyFieldsEqualityActualExpected(actual, expected);
    }

    @Test
    void shouldThrowWhenExternalIdIsNotUnique() {
        val stateDto = TestDataProvider.getTransactionStateDto();
        when(transactionService.existsByExternalIdAndProvider(
                stateDto.getExternalId(), stateDto.getProvider())).thenReturn(true);
        when(exceptionHelper.buildTransactionException(
                "flow.external-id-provider-non-uniqueness",
                stateDto.getExternalId(), stateDto.getProvider())).thenThrow(TransactionException.builder().build());

        assertThrows(TransactionException.class, () -> underTest.createTransaction(stateDto));
    }

    @Test
    void shouldThrowWhenAccountIdDoesntExist() {
        val stateDto = TestDataProvider.getTransactionStateDto();
        when(accountService.getById(stateDto.getUser())).thenThrow(ExternalException.builder().build());
        assertThrows(ExternalException.class, () -> underTest.createTransaction(stateDto));
        verify(accountService).getById(stateDto.getUser());
    }

    @Test
    void shouldThrowWhenTransactionStatusCannotBeChanged() {
        val updateDto = TestDataProvider.getTransactionInfoDto();
        val infoDto = TestDataProvider.getTransactionInfoDto();
        infoDto.setStatus(TransactionStatus.COMPLETED);

        when(transactionService.getByExternalIdAndProvider(
                updateDto.getExternalId(), updateDto.getProvider())).thenReturn(infoDto);
        when(exceptionHelper.buildTransactionException("flow.transaction-status-incorrectness",
                infoDto.getExternalId(), infoDto.getProvider(), infoDto.getStatus()))
                .thenThrow(TransactionException.builder().build());

        assertThrows(TransactionException.class, () -> underTest.updateTransaction(updateDto));
    }

    @Test
    void shouldUpdateTransaction() {
        val updateDto = TestDataProvider.getTransactionInfoDto();
        val existingInfoDto = TestDataProvider.getTransactionInfoDto();
        val expected = TransactionInfoDto.builder()
                .externalId(updateDto.getExternalId())
                .status(updateDto.getStatus())
                .provider(updateDto.getProvider())
                .additionalData(updateDto.getAdditionalData())
                .build();

        when(transactionService.getByExternalIdAndProvider(updateDto.getExternalId(), updateDto.getProvider()))
                .thenReturn(existingInfoDto);
        when(transactionService.update(updateDto)).thenReturn(expected);

        val actual = underTest.updateTransaction(updateDto);

        verify(transactionService, times(1)).update(updateDto);
        AssertionsHelper.verifyFieldsEqualityActualExpected(actual, expected);
    }

    @Test
    void shouldSearchTransactions() {
        val infoDto = TestDataProvider.getTransactionInfoDto();

        when(transactionService.getByExternalIdAndProvider(infoDto.getExternalId(), infoDto.getProvider()))
                .thenReturn(infoDto);

        val actual = underTest.searchTransactions(infoDto.getExternalId(), infoDto.getProvider());

        assertThat(actual).hasSize(1);
        assertThat(actual.get(0)).isEqualTo(infoDto);
    }
}
