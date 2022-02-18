package com.itransition.payment;

import com.itransition.payment.transaction.entity.Transaction;
import com.itransition.payment.account.dto.AccountDto;
import com.itransition.payment.security.dto.AuthResponse;
import com.itransition.payment.core.dto.TransactionInfoDto;
import com.itransition.payment.core.dto.TransactionStateDto;

import static org.assertj.core.api.Assertions.assertThat;

public final class AssertionsHelper {

    public static void verifyFieldsEqualityActualExpected(TransactionInfoDto actual, TransactionInfoDto expected) {
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getExternalId()).isEqualTo(expected.getExternalId());
        assertThat(actual.getProvider()).isEqualTo(expected.getProvider());
        assertThat(actual.getStatus()).isEqualTo(expected.getStatus());
        assertThat(actual.getAdditionalData()).isEqualTo(expected.getAdditionalData());
    }

    public static void verifyFieldsEqualityActualExpected(TransactionStateDto actual, TransactionStateDto expected) {
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getExternalId()).isEqualTo(expected.getExternalId());
        assertThat(actual.getProvider()).isEqualTo(expected.getProvider());
        assertThat(actual.getStatus()).isEqualTo(expected.getStatus());
        assertThat(actual.getAmount()).isEqualTo(expected.getAmount());
        assertThat(actual.getCommissionAmount()).isEqualTo(expected.getCommissionAmount());
        assertThat(actual.getAdditionalData()).isEqualTo(expected.getAdditionalData());
    }

    public static void verifyFieldsEqualityActualExpected(Transaction actual, Transaction expected) {
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getExternalId()).isEqualTo(expected.getExternalId());
        assertThat(actual.getProvider().getName()).isEqualTo(expected.getProvider().getName());
        assertThat(actual.getStatus()).isEqualTo(expected.getStatus());
        assertThat(actual.getReplenishmentStatus()).isEqualTo(expected.getReplenishmentStatus());
        assertThat(actual.getAmount()).isEqualTo(expected.getAmount());
        assertThat(actual.getCurrency()).isEqualTo(expected.getCurrency());
        assertThat(actual.getAdditionalData()).isEqualTo(expected.getAdditionalData());
    }

    public static void verifyFieldsEqualityActualExpected(AccountDto actual, AccountDto expected) {
        assertThat(actual.getFirstName()).isEqualTo(expected.getFirstName());
        assertThat(actual.getLastName()).isEqualTo(expected.getLastName());
        assertThat(actual.getMiddleName()).isEqualTo(expected.getMiddleName());
    }

    public static void verifyFieldsEqualityActualExpected(AuthResponse actual, AuthResponse expected) {
        assertThat(actual.getAccessToken()).isEqualTo(expected.getAccessToken());
        assertThat(actual.getExpiresIn()).isEqualTo(expected.getExpiresIn());
        assertThat(actual.getRefreshExpiresIn()).isEqualTo(expected.getRefreshExpiresIn());
        assertThat(actual.getTokenType()).isEqualTo(expected.getTokenType());
        assertThat(actual.getNotBeforePolicy()).isEqualTo(expected.getNotBeforePolicy());
        assertThat(actual.getScope()).isEqualTo(expected.getScope());
    }
}
