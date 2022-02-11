package com.itransition.payment.core.it.service;

import com.itransition.payment.core.AssertionsHelper;
import com.itransition.payment.core.TestDataProvider;
import com.itransition.payment.core.domain.PaymentProvider;
import com.itransition.payment.core.domain.Transaction;
import com.itransition.payment.core.domain.enums.ReplenishmentStatus;
import com.itransition.payment.core.domain.enums.TransactionStatus;
import com.itransition.payment.core.dto.TransactionUpdateDto;
import com.itransition.payment.core.it.AbstractIntegrationTest;
import com.itransition.payment.core.repository.TransactionRepository;
import com.itransition.payment.core.service.FlowService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@Sql(value = {"classpath:sql/init-db.sql"}, executionPhase = BEFORE_TEST_METHOD)
@Sql(value = {"classpath:sql/drop-schema.sql"}, executionPhase = AFTER_TEST_METHOD)
@Transactional
class FlowServiceIT extends AbstractIntegrationTest {

    @Autowired
    private FlowService underTest;

    @Autowired
    private TransactionRepository transactionRepository;

    @Value("${test.external-id}")
    private String externalId;

    @Value("${test.provider}")
    private String provider;

    @Test
    void shouldCreateTransaction() {
        var stateDto = TestDataProvider.getTransactionAdapterStateDto();
        var expected = Transaction.builder()
                .id(1L)
                .externalId(stateDto.getExternalId())
                .provider(PaymentProvider.builder().name(stateDto.getProvider()).build())
                .status(TransactionStatus.INITIAL)
                .replenishmentStatus(ReplenishmentStatus.INITIAL)
                .amount(stateDto.getAmount().getAmount())
                .currency(stateDto.getAmount().getCurrency())
                .commissionAmount(stateDto.getCommissionAmount().getAmount())
                .commissionCurrency(stateDto.getCommissionAmount().getCurrency())
                .additionalData(stateDto.getAdditionalData())
                .userId(stateDto.getUser())
                .build();

        underTest.createTransaction(stateDto);

        var actual = transactionRepository
                .findByExternalIdAndProviderName(stateDto.getExternalId(), stateDto.getProvider());

        AssertionsHelper.verifyFieldsEqualityActualExpected(actual.get(), expected);
    }

    @Test
    void shouldUpdateTransaction() {
        var existingTransaction = transactionRepository.getById(0L);

        underTest.updateTransaction(TransactionUpdateDto.builder()
                .externalId(existingTransaction.getExternalId())
                .status(TransactionStatus.COMPLETED)
                .provider(existingTransaction.getProvider().getName())
                .additionalData(existingTransaction.getAdditionalData())
                .build());

        var actual = transactionRepository.getById(0L);

        assertThat(actual.getStatus()).isEqualTo(TransactionStatus.COMPLETED);
    }

    @Test
    void shouldSearchTransactions() {
        var expected = transactionRepository.findByExternalIdAndProviderName(externalId, provider).get();

        var actual = underTest.searchTransactions(externalId, provider);

        assertThat(actual.size()).isEqualTo(1);
        assertThat(actual.get(0).getExternalId()).isEqualTo(expected.getExternalId());
        assertThat(actual.get(0).getProvider()).isEqualTo(expected.getProvider().getName());
    }
}
