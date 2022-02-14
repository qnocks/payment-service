package com.itransition.payment.core.unit.mapper;

import com.itransition.payment.core.AssertionsHelper;
import com.itransition.payment.core.TestDataProvider;
import com.itransition.payment.core.domain.PaymentProvider;
import com.itransition.payment.core.domain.Transaction;
import com.itransition.payment.core.dto.TransactionInfoDto;
import com.itransition.payment.core.mapper.TransactionMapperImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TransactionMapperTest {

    @InjectMocks
    private TransactionMapperImpl underTest;

    @Test
    void shouldMapTransactionToTransactionInfoDto() {
        var transaction = TestDataProvider.getTransaction();
        var expected = TransactionInfoDto.builder()
                .id(transaction.getId())
                .externalId(transaction.getExternalId())
                .status(transaction.getStatus())
                .provider(transaction.getProvider().getName())
                .additionalData(transaction.getAdditionalData())
                .build();

        var actual = underTest.toDto(transaction);

        AssertionsHelper.verifyFieldsEqualityActualExpected(actual, expected);
    }

    @Test
    void shouldMapTransactionInfoDtoToTransaction() {
        var infoDto = TestDataProvider.getTransactionInfoDto();
        var expected = Transaction.builder()
                .id(infoDto.getId())
                .externalId(infoDto.getExternalId())
                .status(infoDto.getStatus())
                .provider(PaymentProvider.builder().name(infoDto.getProvider()).build())
                .additionalData(infoDto.getAdditionalData())
                .build();

        var actual = underTest.toEntity(infoDto);

        AssertionsHelper.verifyFieldsEqualityActualExpected(actual, expected);
    }

    @Test
    void shouldMapTransactionUpdateDtoToTransaction() {
        var updateDto = TestDataProvider.getTransactionInfoDto();
        updateDto.setId(null);
        var expected = Transaction.builder()
                .externalId(updateDto.getExternalId())
                .status(updateDto.getStatus())
                .provider(PaymentProvider.builder().name(updateDto.getProvider()).build())
                .additionalData(updateDto.getAdditionalData())
                .build();

        var actual = underTest.toEntity(updateDto);

        AssertionsHelper.verifyFieldsEqualityActualExpected(actual, expected);
    }

    @Test
    void shouldMapTransactionAdapterStateDtoToTransaction() {
        var stateDto = TestDataProvider.getTransactionAdapterStateDto();
        var expected = Transaction.builder()
                .externalId(stateDto.getExternalId())
                .provider(PaymentProvider.builder().name(stateDto.getProvider()).build())
                .amount(stateDto.getAmount().getAmount())
                .currency(stateDto.getAmount().getCurrency())
                .commissionAmount(stateDto.getCommissionAmount().getAmount())
                .commissionCurrency(stateDto.getCommissionAmount().getCurrency())
                .userId(stateDto.getUser())
                .additionalData(stateDto.getAdditionalData())
                .build();

        var actual = underTest.toEntity(stateDto);

        AssertionsHelper.verifyFieldsEqualityActualExpected(actual, expected);
    }
}