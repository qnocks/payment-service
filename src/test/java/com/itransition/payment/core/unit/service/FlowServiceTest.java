package com.itransition.payment.core.unit.service;

import com.itransition.payment.core.AssertionsHelper;
import com.itransition.payment.core.TestDataProvider;
import com.itransition.payment.core.domain.enums.TransactionStatus;
import com.itransition.payment.core.dto.AccountDto;
import com.itransition.payment.core.dto.TransactionInfoDto;
import com.itransition.payment.core.exception.ExceptionMessageResolver;
import com.itransition.payment.core.service.AccountService;
import com.itransition.payment.core.service.TransactionService;
import com.itransition.payment.core.service.impl.FlowServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
    private ExceptionMessageResolver exceptionMessageResolver;

    @Test
    void shouldCreateTransaction() {
        var stateDto = TestDataProvider.getTransactionAdapterStateDto();
        var expected = TestDataProvider.getTransactionInfoDto();

        when(transactionService.existsByExternalIdAndProvider(
                stateDto.getExternalId(), stateDto.getProvider())).thenReturn(false);
        when(accountService.getById(stateDto.getUser())).thenReturn(AccountDto.builder().build());
        when(transactionService.save(stateDto)).thenReturn(expected);

        var actual = underTest.createTransaction(stateDto);

        verify(transactionService, times(1)).save(stateDto);
        AssertionsHelper.verifyFieldsEqualityActualExpected(actual, expected);
    }

    @Test
    void shouldThrow_when_externalIdIsNotUnique() {
        var stateDto = TestDataProvider.getTransactionAdapterStateDto();
        when(transactionService.existsByExternalIdAndProvider(
                stateDto.getExternalId(), stateDto.getProvider())).thenReturn(true);

        // TODO: Should be changed to custom exception when implementation of exception handling
        assertThrows(IllegalStateException.class, () -> underTest.createTransaction(stateDto));
    }

    @Test
    void shouldThrow_when_accountIdDoesntExist() {
        var stateDto = TestDataProvider.getTransactionAdapterStateDto();
        when(accountService.getById(stateDto.getUser())).thenReturn(null);

        // TODO: Should be changed to custom exception when implementation of exception handling
        assertThrows(IllegalStateException.class, () -> underTest.createTransaction(stateDto));
    }

    @Test
    void shouldThrow_when_transactionStatusCannotBeChanged() {
        var updateDto = TestDataProvider.getTransactionInfoDto();
        var infoDto = TestDataProvider.getTransactionInfoDto();
        infoDto.setStatus(TransactionStatus.COMPLETED);

        when(transactionService.getByExternalIdAndProvider(
                updateDto.getExternalId(), updateDto.getProvider())).thenReturn(infoDto);

        // TODO: Should be changed to custom exception when implementation of exception handling
        assertThrows(IllegalStateException.class, () -> underTest.updateTransaction(updateDto));
    }

    @Test
    void shouldUpdateTransaction() {
        var updateDto = TestDataProvider.getTransactionInfoDto();
        var existingInfoDto = TestDataProvider.getTransactionInfoDto();
        var expected = TransactionInfoDto.builder()
                .externalId(updateDto.getExternalId())
                .status(updateDto.getStatus())
                .provider(updateDto.getProvider())
                .additionalData(updateDto.getAdditionalData())
                .build();

        when(transactionService.getByExternalIdAndProvider(updateDto.getExternalId(), updateDto.getProvider()))
                .thenReturn(existingInfoDto);
        when(transactionService.update(updateDto)).thenReturn(expected);

        var actual = underTest.updateTransaction(updateDto);

        verify(transactionService, times(1)).update(updateDto);
        AssertionsHelper.verifyFieldsEqualityActualExpected(actual, expected);
    }

    @Test
    void shouldSearchTransactions() {
        var infoDto = TestDataProvider.getTransactionInfoDto();

        when(transactionService.getByExternalIdAndProvider(infoDto.getExternalId(), infoDto.getProvider()))
                .thenReturn(infoDto);

        var actual = underTest.searchTransactions(infoDto.getExternalId(), infoDto.getProvider());

        assertThat(actual.size()).isEqualTo(1);
        assertThat(actual.get(0)).isEqualTo(infoDto);
    }
}
