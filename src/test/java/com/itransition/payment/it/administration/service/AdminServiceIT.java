package com.itransition.payment.it.administration.service;

import com.itransition.payment.AssertionsHelper;
import com.itransition.payment.core.types.TransactionStatus;
import com.itransition.payment.transaction.dto.AmountDto;
import com.itransition.payment.core.dto.TransactionStateDto;
import com.itransition.payment.it.AbstractIntegrationTest;
import com.itransition.payment.core.repository.TransactionRepository;
import com.itransition.payment.administration.service.AdminService;
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
class AdminServiceIT extends AbstractIntegrationTest {

    @Autowired
    private AdminService underTest;

    @Autowired
    private TransactionRepository transactionRepository;

    @Value("${test.external-id}")
    private String externalId;

    @Value("${test.provider}")
    private String provider;

    @Test
    void shouldSearchTransactions() {
        var existingTransaction = transactionRepository
                .findByExternalIdAndProviderName(externalId, provider).get();

        // TODO: Should be added real pagination params when there will be pagination feature
        var actual = underTest.searchTransactions(0, 0, "", "", "");

        assertThat(actual.size()).isEqualTo(1);
        assertThat(actual.get(0).getId()).isEqualTo(existingTransaction.getId());
    }

    @Test
    void shouldUpdateTransaction() {
        var existingTransaction = transactionRepository.getById(0L);

        underTest.updateTransaction(TransactionStateDto.builder()
                .id(existingTransaction.getId())
                .externalId(existingTransaction.getExternalId())
                .provider(existingTransaction.getProvider().getName())
                .status(existingTransaction.getStatus())
                .amount(AmountDto.builder()
                        .amount(existingTransaction.getAmount())
                        .currency(existingTransaction.getCurrency())
                        .build())
                .commissionAmount(AmountDto.builder()
                        .amount(existingTransaction.getCommissionAmount())
                        .currency(existingTransaction.getCommissionCurrency())
                        .build())
                .additionalData("updated")
                .build());

        var actual = transactionRepository.getById(0L);

        AssertionsHelper.verifyFieldsEqualityActualExpected(actual, existingTransaction);
        assertThat(actual.getAdditionalData()).isEqualTo("updated");
    }

    @Test
    void shouldCompleteTransaction() {
        var existingTransaction = transactionRepository.getById(0L);
        underTest.completeTransaction(
                existingTransaction.getExternalId(),
                existingTransaction.getProvider().getName());
        var actual = transactionRepository.getById(0L);
        assertThat(actual.getStatus()).isEqualTo(TransactionStatus.COMPLETED);
    }
}
