package com.itransition.payment.core.service;

import com.itransition.payment.core.AssertionsHelper;
import com.itransition.payment.core.TestDataProvider;
import com.itransition.payment.core.domain.PaymentProvider;
import com.itransition.payment.core.domain.Transaction;
import com.itransition.payment.core.domain.enums.TransactionStatus;
import com.itransition.payment.core.dto.TransactionInfoDto;
import com.itransition.payment.core.exception.ExceptionMessageResolver;
import com.itransition.payment.core.mapper.TransactionMapper;
import com.itransition.payment.core.repository.TransactionRepository;
import com.itransition.payment.core.service.impl.TransactionServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
        var existingTransaction = TestDataProvider.getTransaction();
        var expected = TransactionInfoDto.builder()
                .id(existingTransaction.getId())
                .externalId(existingTransaction.getExternalId())
                .status(TransactionStatus.COMPLETED)
                .provider(existingTransaction.getProvider().getName())
                .additionalData(existingTransaction.getAdditionalData())
                .build();

        var transaction = Transaction.builder()
                .id(expected.getId())
                .externalId(expected.getExternalId())
                .status(TransactionStatus.COMPLETED)
                .provider(PaymentProvider.builder().name(expected.getProvider()).build())
                .additionalData(expected.getAdditionalData())
                .build();

        when(transactionMapper.toEntity(expected)).thenReturn(transaction);
        when(paymentProviderService.getByProvider(transaction.getProvider().getName()))
                .thenReturn(transaction.getProvider());
        when(transactionRepository.findById(transaction.getId()))
                .thenReturn(Optional.of(existingTransaction));
        when(transactionMapper.toDto(existingTransaction)).thenReturn(expected);

        var actual = underTest.update(expected);

        verify(transactionRepository, times(1)).findById(transaction.getId());
        verify(transactionRepository, times(1)).save(existingTransaction);

        AssertionsHelper.verifyFieldsEqualityActualExpected(actual, expected);
    }

    @Test
    void shouldThrow_when_updatingTransactionDoesntExist() {
        var expected = TestDataProvider.getTransactionInfoDto();
        var transaction = Transaction.builder()
                .id(expected.getId())
                .externalId(expected.getExternalId())
                .status(TransactionStatus.COMPLETED)
                .provider(PaymentProvider.builder().name(expected.getProvider()).build())
                .additionalData(expected.getAdditionalData())
                .build();

        when(transactionMapper.toEntity(expected)).thenReturn(transaction);
        when(transactionRepository.findById(expected.getId())).thenReturn(Optional.empty());

        // TODO: Should be changed to custom exception when implementation of exception handling
        assertThrows(IllegalArgumentException.class, () -> underTest.update(expected));
    }

    @Test
    void shouldReturnTrue_when_existsByExternalId() {
        var externalId = "123";
        when(transactionRepository.existsByExternalId(externalId)).thenReturn(true);
        boolean actual = underTest.existsByExternalId(externalId);
        assertThat(actual).isEqualTo(true);
    }

    @Test
    void shouldGetByExternalId() {
        var transaction = TestDataProvider.getTransaction();
        var expected = TestDataProvider.getTransactionInfoDto();

        when(transactionRepository.findByExternalId(transaction.getExternalId()))
                .thenReturn(Optional.of(transaction));
        when(transactionMapper.toDto(transaction)).thenReturn(expected);

        var actual = underTest.getByExternalId(transaction.getExternalId());

        AssertionsHelper.verifyFieldsEqualityActualExpected(actual, expected);
    }

    @Test
    void shouldThrow_when_GetByExternalId() {
        var externalId = "123";
        when(transactionRepository.findByExternalId(externalId)).thenReturn(Optional.empty());

        // TODO: Should be changed to custom exception when implementation of exception handling
        assertThrows(IllegalArgumentException.class, () -> underTest.getByExternalId(externalId));
    }

    @Test
    void shouldGetAllByExternalIdOrProvider() {
        var infoDto = TestDataProvider.getTransactionInfoDto();
        var transaction = TestDataProvider.getTransaction();

        when(transactionRepository.findAllByExternalIdAndProviderName(infoDto.getExternalId(), infoDto.getProvider()))
                .thenReturn(List.of(transaction));
        when(transactionMapper.toDto(transaction)).thenReturn(TestDataProvider.getTransactionInfoDto());

        var actual = underTest
                .getAllByExternalIdOrProvider(infoDto.getExternalId(), infoDto.getProvider());

        assertThat(actual.size()).isEqualTo(1);
        assertThat(actual.get(0)).isEqualTo(infoDto);
    }
}
