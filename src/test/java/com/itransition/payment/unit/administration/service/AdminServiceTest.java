package com.itransition.payment.unit.administration.service;

import com.itransition.payment.AssertionsHelper;
import com.itransition.payment.TestDataProvider;
import com.itransition.payment.core.dto.TransactionStateDto;
import com.itransition.payment.transaction.service.TransactionService;
import com.itransition.payment.administration.service.impl.AdminServiceImpl;
import java.util.List;
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
        var pagedTransactions = List.of(
                TransactionStateDto.builder().id(1L).build(),
                TransactionStateDto.builder().id(2L).build());
        int page = 0;
        int pageSize = 2;
        String sort = "id";
        String order = "ASC";

        when(transactionService.getAll(PageRequest.of(page, pageSize, Sort.Direction.valueOf(order), sort)))
                .thenReturn(pagedTransactions);

        var actual = underTest.searchTransactions(page, pageSize, sort, order, null);

        assertThat(actual.size()).isEqualTo(pageSize);
        assertThat(actual.get(0).getId()).isEqualTo(1L);
        assertThat(actual.get(1).getId()).isEqualTo(2L);
    }

    @Test
    void shouldUpdateTransaction() {
        var stateDto = TestDataProvider.getTransactionStateDto();
        stateDto.setAdditionalData("updated");

        when(transactionService.update(stateDto)).thenReturn(stateDto);

        var actual = underTest.updateTransaction(stateDto);

        AssertionsHelper.verifyFieldsEqualityActualExpected(actual, stateDto);
    }

    @Test
    void shouldCompleteTransaction() {
        TransactionStateDto stateDto = TestDataProvider.getTransactionStateDto();

        when(transactionService.complete(stateDto.getExternalId(), stateDto.getProvider())).thenReturn(stateDto);

        var actual = underTest.completeTransaction(stateDto.getExternalId(), stateDto.getProvider());

        AssertionsHelper.verifyFieldsEqualityActualExpected(actual, stateDto);
    }
}
