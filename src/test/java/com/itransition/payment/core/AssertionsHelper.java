package com.itransition.payment.core;

import com.itransition.payment.core.domain.Transaction;
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
}
