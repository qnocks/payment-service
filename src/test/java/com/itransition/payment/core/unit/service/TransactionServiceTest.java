package com.itransition.payment.core.unit.service;

import com.itransition.payment.core.AssertionsHelper;
import com.itransition.payment.core.TestDataProvider;
import com.itransition.payment.core.domain.PaymentProvider;
import com.itransition.payment.core.domain.Transaction;
import com.itransition.payment.core.domain.enums.TransactionStatus;
import com.itransition.payment.core.dto.TransactionInfoDto;
import com.itransition.payment.core.exception.ExceptionMessageResolver;
import com.itransition.payment.core.mapper.TransactionMapper;
import com.itransition.payment.core.repository.TransactionRepository;
import com.itransition.payment.core.service.PaymentProviderService;
import com.itransition.payment.core.service.impl.TransactionServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @InjectMocks
    private TransactionServiceImpl underTest;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private PaymentProviderService paymentProviderService;

    @Mock
    private TransactionMapper transactionMapper;

    @Mock
    private ExceptionMessageResolver exceptionMessageResolver;

    @Test
    void shouldSaveNewTransaction() {
        var stateDto = TestDataProvider.getTransactionAdapterStateDto();
        var transaction = Transaction.builder()
                .externalId(stateDto.getExternalId())
                .provider(PaymentProvider.builder().name(stateDto.getProvider()).build())
                .amount(stateDto.getAmount().getAmount())
                .currency(stateDto.getAmount().getCurrency())
                .commissionAmount(stateDto.getCommissionAmount().getAmount())
                .commissionCurrency(stateDto.getCommissionAmount().getCurrency())
                .userId(stateDto.getUser())
                .additionalData(stateDto.getAdditionalData())
                .build();

        var expected = TransactionInfoDto.builder()
                .id(transaction.getId())
                .externalId(transaction.getExternalId())
                .status(transaction.getStatus())
                .provider(transaction.getProvider().getName())
                .additionalData(transaction.getAdditionalData())
                .build();

        given(transactionMapper.toEntity(stateDto)).willReturn(transaction);
        given(transactionMapper.toDto(transaction)).willReturn(expected);
        when(paymentProviderService.getByProvider(stateDto.getProvider())).thenReturn(transaction.getProvider());

        var actual = underTest.save(stateDto);

        verify(transactionRepository, times(1)).saveAndFlush(transaction);

        AssertionsHelper.verifyFieldsEqualityActualExpected(actual, expected);
    }

    @Test
    void shouldUpdateTransaction() {
        var updateDto = TestDataProvider.getTransactionInfoDto();
        var transaction = Transaction.builder()
                .externalId(updateDto.getExternalId())
                .status(TransactionStatus.COMPLETED)
                .provider(PaymentProvider.builder().name(updateDto.getProvider()).build())
                .additionalData(updateDto.getAdditionalData())
                .build();
        var expected = TransactionInfoDto.builder()
                .id(1L)
                .externalId(transaction.getExternalId())
                .status(transaction.getStatus())
                .provider(transaction.getProvider().getName())
                .additionalData(transaction.getAdditionalData())
                .build();

        when(transactionMapper.toEntity(updateDto)).thenReturn(transaction);
        when(paymentProviderService.getByProvider(transaction.getProvider().getName()))
                .thenReturn(transaction.getProvider());
        when(transactionRepository.findByExternalIdAndProviderName(
                transaction.getExternalId(),
                transaction.getProvider().getName()))
                .thenReturn(Optional.of(transaction));
        when(transactionMapper.toDto(transaction)).thenReturn(expected);

        var actual = underTest.update(updateDto);

        verify(transactionRepository, times(1))
                .findByExternalIdAndProviderName(transaction.getExternalId(), transaction.getProvider().getName());
        verify(transactionRepository, times(1)).save(transaction);

        AssertionsHelper.verifyFieldsEqualityActualExpected(actual, expected);
    }

    @Test
    void shouldThrow_when_updatingTransactionDoesntExist() {
        var expected = TestDataProvider.getTransactionInfoDto();
        var transaction = Transaction.builder()
                .externalId(expected.getExternalId())
                .status(TransactionStatus.COMPLETED)
                .provider(PaymentProvider.builder().name(expected.getProvider()).build())
                .additionalData(expected.getAdditionalData())
                .build();

        when(transactionMapper.toEntity(expected)).thenReturn(transaction);
        when(transactionRepository.findByExternalIdAndProviderName(
                expected.getExternalId(), expected.getProvider())).thenReturn(Optional.empty());

        // TODO: Should be changed to custom exception when implementation of exception handling
        assertThrows(IllegalArgumentException.class, () -> underTest.update(expected));
    }

    @Test
    void shouldReturnTrue_when_existsByExternalId() {
        var externalId = "123";
        var providerName = "test";
        when(transactionRepository.existsByExternalIdAndProviderName(externalId, providerName)).thenReturn(true);
        boolean actual = underTest.existsByExternalIdAndProvider(externalId, providerName);
        assertThat(actual).isEqualTo(true);
    }

    @Test
    void shouldGetByExternalIdAndProvider() {
        var infoDto = TestDataProvider.getTransactionInfoDto();
        var transaction = TestDataProvider.getTransaction();

        when(transactionRepository.findByExternalIdAndProviderName(infoDto.getExternalId(), infoDto.getProvider()))
                .thenReturn(Optional.of(transaction));
        when(transactionMapper.toDto(transaction)).thenReturn(TestDataProvider.getTransactionInfoDto());

        var actual = underTest.getByExternalIdAndProvider(infoDto.getExternalId(), infoDto.getProvider());

        AssertionsHelper.verifyFieldsEqualityActualExpected(actual, infoDto);
    }

    @Test
    void shouldThrow_when_GetByExternalIdAndProvider() {
        var externalId = "123";
        var providerName = "test";
        when(transactionRepository.findByExternalIdAndProviderName(externalId, providerName))
                .thenReturn(Optional.empty());

        // TODO: Should be changed to custom exception when implementation of exception handling
        assertThrows(IllegalArgumentException.class, () ->
                underTest.getByExternalIdAndProvider(externalId, providerName));
    }
}
