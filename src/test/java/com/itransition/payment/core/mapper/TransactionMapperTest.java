package com.itransition.payment.core.mapper;

import com.itransition.payment.core.domain.Transaction;
import com.itransition.payment.core.dto.TransactionAdapterStateDto;
import com.itransition.payment.core.dto.TransactionInfoDto;
import com.itransition.payment.core.util.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.assertj.core.api.Assertions.assertThat;


class TransactionMapperTest {

    private TransactionMapperImpl underTest;

    @BeforeEach
    void setup() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setSkipNullEnabled(true)
                .setAmbiguityIgnored(true);
        underTest = new TransactionMapperImpl(mapper);
    }

    @Test
    @DisplayName("Should map Transaction To TransactionInfoDto")
    void shouldMapTransactionToTransactionInfoDto() {
        Transaction transaction = TestUtils.transaction();
        TransactionInfoDto expected = TestUtils.getTransactionInfoDto();
        TransactionInfoDto actual = underTest.toDto(transaction);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void shouldMapTransactionInfoDtoToTransaction() {
        TransactionInfoDto infoDto = TestUtils.getTransactionInfoDto();
        Transaction expected = TestUtils.transaction();
        Transaction actual = underTest.toEntity(infoDto);

        assertThat(actual).hasAllNullFieldsOrPropertiesExcept(
                "id", "externalId", "provider", "status", "additionalData");

        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getExternalId()).isEqualTo(expected.getExternalId());
        assertThat(actual.getProvider()).isEqualTo(expected.getProvider());
        assertThat(actual.getStatus()).isEqualTo(expected.getStatus());
        assertThat(actual.getAdditionalData()).isEqualTo(expected.getAdditionalData());
    }

    @Test
    void shouldMapTransactionAdapterStateDtoToTransaction() {
        TransactionAdapterStateDto adapterStateDto = TestUtils.transactionAdapterStateDto();
        Transaction expected = TestUtils.transaction();
        Transaction actual = underTest.toEntity(adapterStateDto);

        assertThat(actual).hasAllNullFieldsOrPropertiesExcept("externalId", "provider", "amount",
                "currency", "commissionAmount", "commissionCurrency", "userId", "additionalData");

        assertThat(actual.getExternalId()).isEqualTo(expected.getExternalId());
        assertThat(actual.getProvider()).isEqualTo(expected.getProvider());
        assertThat(actual.getAmount()).isEqualTo(expected.getAmount());
        assertThat(actual.getCurrency()).isEqualTo(expected.getCurrency());
        assertThat(actual.getCommissionAmount()).isEqualTo(expected.getCommissionAmount());
        assertThat(actual.getCommissionCurrency()).isEqualTo(expected.getCommissionCurrency());
        assertThat(actual.getUserId()).isEqualTo(expected.getUserId());
        assertThat(actual.getAdditionalData()).isEqualTo(expected.getAdditionalData());
    }
}
