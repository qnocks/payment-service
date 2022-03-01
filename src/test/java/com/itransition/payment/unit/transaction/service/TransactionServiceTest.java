package com.itransition.payment.unit.transaction.service;

import com.itransition.payment.AssertionsHelper;
import com.itransition.payment.TestDataProvider;
import com.itransition.payment.core.dto.TransactionInfoDto;
import com.itransition.payment.core.dto.TransactionStateDto;
import com.itransition.payment.core.exception.ExceptionHelper;
import com.itransition.payment.core.exception.custom.TransactionException;
import com.itransition.payment.core.repository.TransactionRepository;
import com.itransition.payment.core.type.ReplenishmentStatus;
import com.itransition.payment.core.type.TransactionStatus;
import com.itransition.payment.transaction.entity.PaymentProvider;
import com.itransition.payment.transaction.entity.Transaction;
import com.itransition.payment.transaction.mapper.TransactionMapper;
import com.itransition.payment.transaction.service.PaymentProviderService;
import com.itransition.payment.transaction.service.impl.TransactionServiceImpl;
import java.util.List;
import java.util.Optional;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
        val stateDto = TestDataProvider.getTransactionStateDto();
        val transaction = Transaction.builder()
                .externalId(stateDto.getExternalId())
                .provider(PaymentProvider.builder().name(stateDto.getProvider()).build())
                .amount(stateDto.getAmount().getAmount())
                .currency(stateDto.getAmount().getCurrency())
                .commissionAmount(stateDto.getCommissionAmount().getAmount())
                .commissionCurrency(stateDto.getCommissionAmount().getCurrency())
                .userId(stateDto.getUser())
                .additionalData(stateDto.getAdditionalData())
                .build();

        val expected = TransactionInfoDto.builder()
                .id(transaction.getId())
                .externalId(transaction.getExternalId())
                .status(transaction.getStatus())
                .provider(transaction.getProvider().getName())
                .additionalData(transaction.getAdditionalData())
                .build();

        given(transactionMapper.toEntity(stateDto)).willReturn(transaction);
        given(transactionMapper.toDto(transaction)).willReturn(expected);
        when(paymentProviderService.getByProvider(stateDto.getProvider())).thenReturn(transaction.getProvider());

        val actual = underTest.save(stateDto);

        verify(transactionRepository, times(1)).saveAndFlush(transaction);

        AssertionsHelper.verifyFieldsEqualityActualExpected(actual, expected);
    }

    @Test
    void shouldUpdateTransaction() {
        val updateDto = TestDataProvider.getTransactionInfoDto();
        val transaction = Transaction.builder()
                .externalId(updateDto.getExternalId())
                .status(TransactionStatus.COMPLETED)
                .provider(PaymentProvider.builder().name(updateDto.getProvider()).build())
                .additionalData(updateDto.getAdditionalData())
                .build();
        val expected = TransactionInfoDto.builder()
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

        val actual = underTest.update(updateDto);

        verify(transactionRepository, times(1))
                .findByExternalIdAndProviderName(transaction.getExternalId(), transaction.getProvider().getName());
        verify(transactionRepository, times(1)).save(transaction);

        AssertionsHelper.verifyFieldsEqualityActualExpected(actual, expected);
    }

    @Test
    void shouldThrowWhenUpdatingTransactionDoesntExist() {
        val expected = TestDataProvider.getTransactionInfoDto();
        val transaction = Transaction.builder()
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
        val externalId = "123";
        val providerName = "test";
        when(transactionRepository.existsByExternalIdAndProviderName(externalId, providerName)).thenReturn(true);
        boolean actual = underTest.existsByExternalIdAndProvider(externalId, providerName);
        assertThat(actual).isTrue();
    }

    @Test
    void shouldGetByExternalIdAndProvider() {
        val infoDto = TestDataProvider.getTransactionInfoDto();
        val transaction = TestDataProvider.getTransaction();

        when(transactionRepository.findByExternalIdAndProviderName(infoDto.getExternalId(), infoDto.getProvider()))
                .thenReturn(Optional.of(transaction));
        when(transactionMapper.toDto(transaction)).thenReturn(TestDataProvider.getTransactionInfoDto());

        val actual = underTest.getByExternalIdAndProvider(infoDto.getExternalId(), infoDto.getProvider());

        AssertionsHelper.verifyFieldsEqualityActualExpected(actual, infoDto);
    }

    @Test
    void shouldThrowWhenGetByExternalIdAndProvider() {
        val externalId = "123";
        val providerName = "test";
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
        val transaction = TestDataProvider.getTransaction();
        transaction.setStatus(TransactionStatus.COMPLETED);
        val expected = TestDataProvider.getTransactionReplenishDto();

        when(transactionRepository.findAllByStatusAndReplenishmentStatusOrderByIdAsc(
                TransactionStatus.COMPLETED, ReplenishmentStatus.INITIAL)).thenReturn(List.of(transaction));
        when(transactionMapper.toReplenishDto(transaction)).thenReturn(expected);

        val actual = underTest.getReadyToReplenish();

        AssertionsHelper.verifyFieldsEqualityActualExpected(actual, expected);
    }

    @Test
    void shouldGetPageableResult() {
        val pagedTransactions = List.of(
                Transaction.builder().id(1L).build(),
                Transaction.builder().id(2L).build());

        int pageSize = pagedTransactions.size();

        val pageable = PageRequest.of(0, pageSize, Sort.Direction.valueOf("ASC"), "id");

        when(transactionRepository.findAll(pageable)).thenReturn(new PageImpl<>(pagedTransactions, pageable, pageSize));
        when(transactionMapper.toAdminDto(any(Transaction.class))).thenReturn(TransactionStateDto.builder().build());

        val actual = underTest.getAll(pageable);

        assertThat(actual).hasSize(pagedTransactions.size());
    }

    @Test
    void shouldGetNullWhenNothingReadyToReplenish() {
        when(transactionRepository.findAllByStatusAndReplenishmentStatusOrderByIdAsc(
                TransactionStatus.COMPLETED, ReplenishmentStatus.INITIAL))
                .thenReturn(List.of(TestDataProvider.getTransaction()));

        val actual = underTest.getReadyToReplenish();

        assertThat(actual).isNull();
    }

    @Test
    void shouldUpdateReplenishStatus() {
        val replenishDto = TestDataProvider.getTransactionReplenishDto();
        val transaction = TestDataProvider.getTransaction();

        when(transactionMapper.toEntity(replenishDto)).thenReturn(transaction);
        when(paymentProviderService.getByProvider(transaction.getProvider().getName()))
                .thenReturn(transaction.getProvider());
        when(transactionRepository.findByExternalIdAndProviderName(
                transaction.getExternalId(), transaction.getProvider().getName())).thenReturn(Optional.of(transaction));

        underTest.updateReplenishStatus(replenishDto, ReplenishmentStatus.FAILED);

        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    void shouldSetReplenishAfter() {
        val replenishDto = TestDataProvider.getTransactionReplenishDto();
        val transaction = TestDataProvider.getTransaction();

        when(transactionMapper.toEntity(replenishDto)).thenReturn(transaction);
        when(paymentProviderService.getByProvider(transaction.getProvider().getName()))
                .thenReturn(transaction.getProvider());
        when(transactionRepository.findByExternalIdAndProviderName(
                transaction.getExternalId(), transaction.getProvider().getName())).thenReturn(Optional.of(transaction));

        underTest.setReplenishAfter(replenishDto, 10.25);

        verify(transactionRepository, times(1)).save(transaction);
    }
}
