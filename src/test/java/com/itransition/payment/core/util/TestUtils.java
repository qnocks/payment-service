package com.itransition.payment.core.util;

import com.itransition.payment.core.domain.PaymentProvider;
import com.itransition.payment.core.domain.Transaction;
import com.itransition.payment.core.domain.enums.TransactionStatus;
import com.itransition.payment.core.dto.AmountDto;
import com.itransition.payment.core.dto.TransactionAdapterStateDto;
import com.itransition.payment.core.dto.TransactionInfoDto;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class TestUtils {

    private static final Long ID = 1L;
    private static final String EXTERNAL_ID = "123";
    private static final TransactionStatus STATUS = TransactionStatus.INITIAL;
    private static final PaymentProvider PROVIDER = PaymentProvider.builder().id(1L).provider("test").build();
    private static final BigDecimal AMOUNT = BigDecimal.valueOf(123.123);
    private static final String CURRENCY = "USD";
    private static final BigDecimal COMMISSION_AMOUNT = BigDecimal.valueOf(12.12);
    private static final String COMMISSION_CURRENCY = "USD";
    private static final String USER_ID = "321";
    private static final LocalDateTime CREATED_AT = LocalDateTime.of(2022, 2, 8, 3, 45);
    private static final LocalDateTime UPDATED_AT = LocalDateTime.of(2022, 2, 8, 3, 45);
    private static final String ADDITIONAL_DATA = "{}";

    public static Transaction transaction() {
        return Transaction.builder()
                .id(ID)
                .externalId(EXTERNAL_ID)
                .provider(PROVIDER)
                .status(STATUS)
                .amount(AMOUNT)
                .currency(CURRENCY)
                .commissionAmount(COMMISSION_AMOUNT)
                .commissionCurrency(COMMISSION_CURRENCY)
                .userId(USER_ID)
                .createdAt(CREATED_AT)
                .updatedAt(UPDATED_AT)
                .additionalData(ADDITIONAL_DATA)
                .build();
    }

    public static Transaction transactionAdapterStateDtoToTransaction() {
        return null;
    }

    public static TransactionAdapterStateDto transactionAdapterStateDto() {
        return TransactionAdapterStateDto.builder()
                .externalId(EXTERNAL_ID)
                .provider(PROVIDER.getProvider())
                .amount(AmountDto.builder()
                        .amount(AMOUNT)
                        .currency(CURRENCY)
                        .build())
                .commissionAmount(AmountDto.builder()
                        .amount(COMMISSION_AMOUNT)
                        .currency(COMMISSION_CURRENCY)
                        .build())
                .user(USER_ID)
                .additionalData(ADDITIONAL_DATA)
                .build();
    }

    public static TransactionInfoDto getTransactionInfoDto() {
        return TransactionInfoDto.builder()
                .id(ID)
                .externalId(EXTERNAL_ID)
                .status(STATUS)
                .provider(PROVIDER.getProvider())
                .additionalData(ADDITIONAL_DATA)
                .build();
    }

    public static Transaction copy(Transaction transaction) {
        return Transaction.builder()
                .id(transaction.getId())
                .externalId(transaction.getExternalId())
                .provider(transaction.getProvider())
                .status(transaction.getStatus())
                .amount(transaction.getAmount())
                .currency(transaction.getCurrency())
                .commissionAmount(transaction.getCommissionAmount())
                .commissionCurrency(transaction.getCommissionCurrency())
                .userId(transaction.getUserId())
                .createdAt(transaction.getCreatedAt())
                .updatedAt(transaction.getUpdatedAt())
                .additionalData(transaction.getAdditionalData())
                .build();
    }

    public static TransactionInfoDto copy(TransactionInfoDto infoDto) {
        return TransactionInfoDto.builder()
                .id(infoDto.getId())
                .externalId(infoDto.getExternalId())
                .status(infoDto.getStatus())
                .provider(infoDto.getProvider())
                .additionalData(infoDto.getAdditionalData())
                .build();
    }
}
