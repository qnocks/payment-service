package com.itransition.payment.it.transaction.mapper;

import com.itransition.payment.AssertionsHelper;
import com.itransition.payment.TestDataProvider;
import com.itransition.payment.core.dto.TransactionInfoDto;
import com.itransition.payment.core.dto.TransactionReplenishDto;
import com.itransition.payment.core.dto.TransactionStateDto;
import com.itransition.payment.it.AbstractIntegrationTest;
import com.itransition.payment.transaction.dto.AmountDto;
import com.itransition.payment.transaction.entity.PaymentProvider;
import com.itransition.payment.transaction.entity.Transaction;
import com.itransition.payment.transaction.mapper.TransactionMapperOrikaImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TransactionMapperIT extends AbstractIntegrationTest {

    @Autowired
    private TransactionMapperOrikaImpl underTest;

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
    void shouldMapTransactionToReplenishDto() {
        var transaction = TestDataProvider.getTransaction();
        var expected = TransactionReplenishDto.builder()
                .provider(transaction.getProvider().getName())
                .outerId(transaction.getExternalId())
                .gateId(String.valueOf(transaction.getId()))
                .outerAt(transaction.getExternalDate())
                .account(Integer.valueOf(transaction.getUserId()))
                .amount(transaction.getAmount())
                .commissionAmount(transaction.getCommissionAmount())
                .build();

        var actual = underTest.toReplenishDto(transaction);

        AssertionsHelper.verifyFieldsEqualityActualExpected(actual, expected);
    }

    @Test
    void shouldMapTransactionToTransactionStateDto() {
        var transaction = TestDataProvider.getTransaction();
        var expected = TransactionStateDto.builder()
                .id(transaction.getId())
                .externalId(transaction.getExternalId())
                .provider(transaction.getProvider().getName())
                .status(transaction.getStatus())
                .amount(AmountDto.builder()
                        .amount(transaction.getAmount())
                        .currency(transaction.getCurrency())
                        .build())
                .commissionAmount(AmountDto.builder()
                        .amount(transaction.getCommissionAmount())
                        .currency(transaction.getCommissionCurrency())
                        .build())
                .user(transaction.getUserId())
                .additionalData(transaction.getAdditionalData())
                .build();

        var actual = underTest.toAdminDto(transaction);

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
    void shouldMapTransactionStateDtoToTransaction() {
        var stateDto = TestDataProvider.getTransactionStateDto();
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

    @Test
    void shouldMapTransactionReplenishDtoToTransaction() {
        var replenishDto = TestDataProvider.getTransactionReplenishDto();
        var expected = Transaction.builder()
                .id(Long.valueOf(replenishDto.getGateId()))
                .externalId(replenishDto.getOuterId())
                .provider(PaymentProvider.builder().name(replenishDto.getProvider()).build())
                .amount(replenishDto.getAmount())
                .commissionAmount(replenishDto.getCommissionAmount())
                .userId(String.valueOf(replenishDto.getAccount()))
                .externalDate(replenishDto.getOuterAt())
                .build();

        var actual = underTest.toEntity(replenishDto);

        AssertionsHelper.verifyFieldsEqualityActualExpected(actual, expected);
    }

}
