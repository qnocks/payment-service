package com.itransition.payment.unit.administration.service;

import com.itransition.payment.AssertionsHelper;
import com.itransition.payment.TestDataProvider;
import com.itransition.payment.core.dto.TransactionStateDto;
import com.itransition.payment.core.type.TransactionStatus;
import com.itransition.payment.transaction.service.TransactionService;
import com.itransition.payment.administration.service.impl.AdminServiceImpl;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @InjectMocks
    private AdminServiceImpl underTest;

    @Mock
    private TransactionService transactionService;

    @Test
    void shouldSearchTransactions() {
        val pagedTransactions = List.of(
                TransactionStateDto.builder().id(1L).build(),
                TransactionStateDto.builder().id(2L).build());

        int page = 0;
        int pageSize = 2;
        String order = "ASC";
        String sort = "id";

        when(transactionService.getAll(PageRequest.of(page, pageSize, Sort.Direction.valueOf(order), sort)))
                .thenReturn(pagedTransactions);

        val actual = underTest.searchTransactions(
                PageRequest.of(page, pageSize, Sort.Direction.valueOf(order), sort));

        assertThat(actual).hasSize(pageSize);
        assertThat(actual.get(0).getId()).isEqualTo(1L);
        assertThat(actual.get(1).getId()).isEqualTo(2L);
    }

    @Test
    void shouldUpdateTransaction() {
        val stateDto = TestDataProvider.getTransactionStateDto();
        stateDto.setAdditionalData("updated");

        when(transactionService.update(stateDto)).thenReturn(stateDto);

        val actual = underTest.updateTransaction(stateDto);

        AssertionsHelper.verifyFieldsEqualityActualExpected(actual, stateDto);
    }

    @Test
    void shouldCompleteTransaction() {
        val stateDto = TestDataProvider.getTransactionStateDto();
        val completedTransaction = TransactionStateDto.builder()
                .externalId(stateDto.getExternalId())
                .provider(stateDto.getProvider())
                .status(TransactionStatus.COMPLETED)
                .build();

        when(transactionService.update(completedTransaction)).thenReturn(completedTransaction);

        val actual = underTest.completeTransaction(stateDto.getExternalId(), stateDto.getProvider());

        AssertionsHelper.verifyFieldsEqualityActualExpected(actual, completedTransaction);
    }
}
