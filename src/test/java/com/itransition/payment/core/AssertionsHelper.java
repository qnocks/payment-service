package com.itransition.payment.core;

import com.itransition.payment.core.domain.Transaction;
import com.itransition.payment.core.dto.AccountDto;
import com.itransition.payment.core.dto.AuthResponse;
import com.itransition.payment.core.dto.TransactionInfoDto;

import static org.assertj.core.api.Assertions.assertThat;

public final class AssertionsHelper {

    public static void verifyFieldsEqualityActualExpected(TransactionInfoDto actual, TransactionInfoDto expected) {
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getExternalId()).isEqualTo(expected.getExternalId());
        assertThat(actual.getProvider()).isEqualTo(expected.getProvider());
        assertThat(actual.getStatus()).isEqualTo(expected.getStatus());
        assertThat(actual.getAdditionalData()).isEqualTo(expected.getAdditionalData());
    }

    public static void verifyFieldsEqualityActualExpected(Transaction actual, Transaction expected) {
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getExternalId()).isEqualTo(expected.getExternalId());
        assertThat(actual.getProvider()).isEqualTo(expected.getProvider());
        assertThat(actual.getStatus()).isEqualTo(expected.getStatus());
        assertThat(actual.getReplenishmentStatus()).isEqualTo(expected.getReplenishmentStatus());
        assertThat(actual.getAmount()).isEqualTo(expected.getAmount());
        assertThat(actual.getCurrency()).isEqualTo(expected.getCurrency());
        assertThat(actual.getCommissionAmount()).isEqualTo(expected.getCommissionAmount());
        assertThat(actual.getCommissionCurrency()).isEqualTo(expected.getCommissionCurrency());
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
