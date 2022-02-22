package com.itransition.payment.unit.transaction.service;

import com.itransition.payment.AssertionsHelper;
import com.itransition.payment.TestDataProvider;
import com.itransition.payment.core.dto.TransactionInfoDto;
import com.itransition.payment.core.exception.ExceptionHelper;
import com.itransition.payment.core.exception.custom.TransactionException;
import com.itransition.payment.core.repository.TransactionRepository;
import com.itransition.payment.core.types.ReplenishmentStatus;
import com.itransition.payment.core.types.TransactionStatus;
import com.itransition.payment.transaction.entity.PaymentProvider;
import com.itransition.payment.transaction.entity.Transaction;
import com.itransition.payment.transaction.mapper.TransactionMapper;
import com.itransition.payment.transaction.service.PaymentProviderService;
import com.itransition.payment.transaction.service.impl.TransactionServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

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
    private ExceptionHelper exceptionHelper;

    @Test
    void shouldSaveNewTransaction() {
        var stateDto = TestDataProvider.getTransactionStateDto();
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
    void shouldThrowWhenUpdatingTransactionDoesntExist() {
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
        when(exceptionHelper.buildTransactionException(
                HttpStatus.NOT_FOUND,
                "transaction.cannot-get-by-external-id-provider",
                expected.getExternalId(), expected.getProvider())).thenThrow(TransactionException.builder().build());

        assertThrows(TransactionException.class, () -> underTest.update(expected));
    }

    @Test
    void shouldReturnTrueWhenExistsByExternalId() {
        var externalId = "123";
        var providerName = "test";
        when(transactionRepository.existsByExternalIdAndProviderName(externalId, providerName)).thenReturn(true);
        boolean actual = underTest.existsByExternalIdAndProvider(externalId, providerName);
        assertThat(actual).isTrue();
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
    void shouldThrowWhenGetByExternalIdAndProvider() {
        var externalId = "123";
        var providerName = "test";
        when(transactionRepository.findByExternalIdAndProviderName(externalId, providerName))
                .thenReturn(Optional.empty());

        when(exceptionHelper.buildTransactionException(
                HttpStatus.NOT_FOUND,
                "transaction.cannot-get-by-external-id-provider",
                externalId, providerName)).thenThrow(TransactionException.builder().build());

        assertThrows(TransactionException.class, () ->
                underTest.getByExternalIdAndProvider(externalId, providerName));
    }

    @Test
    void shouldGetReadyToReplenish() {
        var transaction = TestDataProvider.getTransaction();
        transaction.setStatus(TransactionStatus.COMPLETED);
        var expected = TestDataProvider.getTransactionReplenishDto();

        when(transactionRepository.findAllByStatusAndReplenishmentStatusOrderByIdAsc(
                TransactionStatus.COMPLETED, ReplenishmentStatus.INITIAL)).thenReturn(List.of(transaction));
        when(transactionMapper.toReplenishDto(transaction)).thenReturn(expected);

        var actual = underTest.getReadyToReplenish();

        AssertionsHelper.verifyFieldsEqualityActualExpected(actual, expected);
    }

    @Test
    void shouldGetNullWhenNothingReadyToReplenish() {
        var transaction = TestDataProvider.getTransaction();

        when(transactionRepository.findAllByStatusAndReplenishmentStatusOrderByIdAsc(
                TransactionStatus.COMPLETED, ReplenishmentStatus.INITIAL)).thenReturn(List.of(transaction));

        var actual = underTest.getReadyToReplenish();

        assertThat(actual).isNull();
    }

    @Test
    void shouldUpdateReplenishStatus() {
        var replenishDto = TestDataProvider.getTransactionReplenishDto();
        var transaction = TestDataProvider.getTransaction();
        var status = ReplenishmentStatus.FAILED;

        when(transactionMapper.toEntity(replenishDto)).thenReturn(transaction);
        when(paymentProviderService.getByProvider(transaction.getProvider().getName()))
                .thenReturn(transaction.getProvider());
        when(transactionRepository.findByExternalIdAndProviderName(
                transaction.getExternalId(), transaction.getProvider().getName())).thenReturn(Optional.of(transaction));

        underTest.updateReplenishStatus(replenishDto, status);

        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    void shouldSetReplenishAfter() {
        double replenishAfter = 10.25;
        var replenishDto = TestDataProvider.getTransactionReplenishDto();
        var transaction = TestDataProvider.getTransaction();

        when(transactionMapper.toEntity(replenishDto)).thenReturn(transaction);
        when(paymentProviderService.getByProvider(transaction.getProvider().getName()))
                .thenReturn(transaction.getProvider());
        when(transactionRepository.findByExternalIdAndProviderName(
                transaction.getExternalId(), transaction.getProvider().getName())).thenReturn(Optional.of(transaction));

        underTest.setReplenishAfter(replenishDto, replenishAfter);

        verify(transactionRepository, times(1)).save(transaction);
    }
}
