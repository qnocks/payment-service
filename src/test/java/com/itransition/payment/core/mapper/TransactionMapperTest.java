package com.itransition.payment.core.mapper;

import com.itransition.payment.core.domain.Transaction;
import com.itransition.payment.core.dto.TransactionAdapterStateDto;
import com.itransition.payment.core.dto.TransactionInfoDto;
import com.itransition.payment.core.TransactionTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionMapperTest {

    private TransactionMapperImpl underTest;

    @BeforeEach
    void setup() {
        underTest = new TransactionMapperImpl();
    }

    @Test
    @DisplayName("Should map Transaction to TransactionInfoDto")
    void shouldMapTransactionToTransactionInfoDto() {
        Transaction transaction = TransactionTestUtils.transaction();
        TransactionInfoDto expected = TransactionTestUtils.transactionInfoDto();
        TransactionInfoDto actual = underTest.toDto(transaction);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should map TransactionInfoDto to Transaction")
    void shouldMapTransactionInfoDtoToTransaction() {
        TransactionInfoDto infoDto = TransactionTestUtils.transactionInfoDto();
        Transaction expected = TransactionTestUtils.transactionInfoDtoToTransaction();
        Transaction actual = underTest.toEntity(infoDto);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should map TransactionAdapterStateDto to Transaction")
    void shouldMapTransactionAdapterStateDtoToTransaction() {
        TransactionAdapterStateDto stateDto = TransactionTestUtils.transactionAdapterStateDto();
        Transaction expected = TransactionTestUtils.transactionAdapterStateDtoToTransaction();
        Transaction actual = underTest.toEntity(stateDto);
        assertThat(actual).isEqualTo(expected);
    }
}
