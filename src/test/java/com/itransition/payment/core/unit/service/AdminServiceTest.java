package com.itransition.payment.core.unit.service;

import com.itransition.payment.core.AssertionsHelper;
import com.itransition.payment.core.TestDataProvider;
import com.itransition.payment.core.dto.TransactionStateDto;
import com.itransition.payment.transaction.service.TransactionService;
import com.itransition.payment.administration.service.impl.AdminServiceImpl;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        var stateDto = TestDataProvider.getTransactionStateDto();
        when(transactionService.getAll()).thenReturn(List.of(stateDto));

        var actual = underTest.searchTransactions(0, 0, "", "", "");

        assertThat(actual.size()).isEqualTo(1);
        AssertionsHelper.verifyFieldsEqualityActualExpected(actual.get(0), stateDto);
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
