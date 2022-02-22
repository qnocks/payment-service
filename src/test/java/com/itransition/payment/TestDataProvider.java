package com.itransition.payment;

import com.itransition.payment.transaction.entity.PaymentProvider;
import com.itransition.payment.transaction.entity.Transaction;
import com.itransition.payment.core.types.ReplenishmentStatus;
import com.itransition.payment.core.types.TransactionStatus;
import com.itransition.payment.account.dto.AccountDto;
import com.itransition.payment.transaction.dto.AmountDto;
import com.itransition.payment.security.dto.AuthResponse;
import com.itransition.payment.core.dto.TransactionReplenishDto;
import com.itransition.payment.core.dto.TransactionStateDto;
import com.itransition.payment.core.dto.TransactionInfoDto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestDataProvider {

    private static final Long ID = 1L;
    private static final String EXTERNAL_ID = "123";
    private static final TransactionStatus STATUS = TransactionStatus.INITIAL;
    private static final ReplenishmentStatus REPLENISHMENT_STATUS = ReplenishmentStatus.INITIAL;
    private static final PaymentProvider PROVIDER = PaymentProvider.builder().id(1L).name("test").build();
    private static final BigDecimal AMOUNT = BigDecimal.valueOf(123.123);
    private static final String CURRENCY = "USD";
    private static final BigDecimal COMMISSION_AMOUNT = BigDecimal.valueOf(12.12);
    private static final String COMMISSION_CURRENCY = "USD";
    private static final String USER_ID = "321";
    private static final LocalDateTime CREATED_AT = LocalDateTime.of(2022, 2, 8, 4, 45);
    private static final LocalDateTime UPDATED_AT = LocalDateTime.of(2022, 2, 8, 4, 45);
    private static final String ADDITIONAL_DATA = "{}";

    public static Transaction getTransaction() {
        return Transaction.builder()
                .id(ID)
                .externalId(EXTERNAL_ID)
                .provider(PROVIDER)
                .status(STATUS)
                .replenishmentStatus(REPLENISHMENT_STATUS)
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

    public static TransactionStateDto getTransactionStateDto() {
        return TransactionStateDto.builder()
                .externalId(EXTERNAL_ID)
                .provider(PROVIDER.getName())
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
                .provider(PROVIDER.getName())
                .additionalData(ADDITIONAL_DATA)
                .build();
    }

    public static TransactionReplenishDto getTransactionReplenishDto() {
        return TransactionReplenishDto.builder()
                .provider(PROVIDER.getName())
                .outerId(EXTERNAL_ID)
                .gateId(String.valueOf(ID))
                .account(Integer.valueOf(USER_ID))
                .amount(AMOUNT)
                .commissionAmount(COMMISSION_AMOUNT)
                .build();
    }

    public static PaymentProvider getPaymentProvider() {
        return PROVIDER;
    }

    public static AuthResponse getAuthResponse() {
        return AuthResponse.builder()
                .accessToken("token")
                .expiresIn(5400)
                .refreshExpiresIn(0)
                .tokenType("Bearer")
                .notBeforePolicy(0)
                .scope("profile email")
                .build();
    }

    public static AccountDto getAccountDto() {
        return AccountDto.builder()
                .firstName("John")
                .lastName("Smith")
                .middleName(null)
                .build();
    }
}
