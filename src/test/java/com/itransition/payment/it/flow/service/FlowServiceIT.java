package com.itransition.payment.it.flow.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.itransition.payment.AssertionsHelper;
import com.itransition.payment.TestDataProvider;
import com.itransition.payment.core.dto.TransactionInfoDto;
import com.itransition.payment.core.repository.TransactionRepository;
import com.itransition.payment.core.type.ReplenishmentStatus;
import com.itransition.payment.core.type.TransactionStatus;
import com.itransition.payment.flow.service.FlowService;
import com.itransition.payment.it.AbstractIntegrationTest;
import com.itransition.payment.transaction.entity.PaymentProvider;
import com.itransition.payment.transaction.entity.Transaction;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @Autowired
    private ObjectMapper mapper;

    @Value("${test.external-id}")
    private String externalId;

    @Value("${test.provider}")
    private String provider;

    @WireMockTest(httpPort = 8082)
    @Nested
    class FlowCreatingTransactionTest {

        @SneakyThrows
        @Test
        void shouldCreateTransaction() {
            WireMock.stubFor(WireMock.get("/account/" + 321).willReturn(
                    ResponseDefinitionBuilder.responseDefinition()
                            .withBody(mapper.writeValueAsString(TestDataProvider.getAccountDto()))
                            .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                            .withStatus(HttpStatus.OK.value())));

            WireMock.stubFor(WireMock.post("/auth/token?grant_type=&client_secret=&client_id=")
                    .willReturn(ResponseDefinitionBuilder.responseDefinition()
                            .withBody(mapper.writeValueAsString(TestDataProvider.getAuthResponse()))
                            .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                            .withStatus(HttpStatus.OK.value())));

            val stateDto = TestDataProvider.getTransactionStateDto();
            val expected = Transaction.builder()
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

            val actual = transactionRepository
                    .findByExternalIdAndProviderName(stateDto.getExternalId(), stateDto.getProvider());

            AssertionsHelper.verifyFieldsEqualityActualExpected(actual.get(), expected);
        }
    }

    @Test
    void shouldUpdateTransaction() {
        val existingTransaction = transactionRepository.getById(0L);

        underTest.updateTransaction(TransactionInfoDto.builder()
                .externalId(existingTransaction.getExternalId())
                .status(TransactionStatus.COMPLETED)
                .provider(existingTransaction.getProvider().getName())
                .additionalData(existingTransaction.getAdditionalData())
                .build());

        val actual = transactionRepository.getById(0L);

        assertThat(actual.getStatus()).isEqualTo(TransactionStatus.COMPLETED);
    }

    @Test
    void shouldSearchTransactions() {
        val expected = transactionRepository.findByExternalIdAndProviderName(externalId, provider).get();
        val actual = underTest.searchTransactions(externalId, provider);

        assertThat(actual).hasSize(1);
        assertThat(actual.get(0).getExternalId()).isEqualTo(expected.getExternalId());
        assertThat(actual.get(0).getProvider()).isEqualTo(expected.getProvider().getName());
    }
}
