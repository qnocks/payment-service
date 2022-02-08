package com.itransition.payment.core.service;

import com.itransition.payment.core.domain.Transaction;
import com.itransition.payment.core.dto.TransactionAdapterStateDto;
import com.itransition.payment.core.dto.TransactionInfoDto;
import com.itransition.payment.core.exception.ExceptionMessageResolver;
import com.itransition.payment.core.mapper.TransactionMapper;
import com.itransition.payment.core.repository.TransactionRepository;
import com.itransition.payment.core.service.impl.TransactionServiceImpl;
import com.itransition.payment.core.util.TestUtils;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @InjectMocks
    private TransactionServiceImpl underTest;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private PaymentProviderService paymentProviderService;

    @Mock
    private TransactionMapper transactionMapper;

    @Mock
    private ExceptionMessageResolver exceptionMessageResolver;

    @Test
    @DisplayName("Should save new Transaction")
    void shouldSaveNewTransaction() {
        TransactionAdapterStateDto stateDto = TestUtils.transactionAdapterStateDto();
        Transaction transaction = TestUtils.transaction();
        TransactionInfoDto infoDto = TestUtils.getTransactionInfoDto();

        given(transactionMapper.toEntity(stateDto)).willReturn(transaction);
        given(transactionMapper.toDto(transaction)).willReturn(TestUtils.copy(infoDto));
        when(paymentProviderService.getByProvider(stateDto.getProvider())).thenReturn(transaction.getProvider());

        TransactionInfoDto actual = underTest.save(stateDto);

        verify(transactionRepository, times(1)).saveAndFlush(transaction);

        assertThat(actual).isEqualTo(infoDto);
    }

    @Test
    @DisplayName("Should update Transaction")
    void shouldUpdateTransaction() {
        Transaction transaction = TestUtils.transaction();
        TransactionInfoDto infoDto = TestUtils.getTransactionInfoDto();

        when(transactionMapper.toEntity(infoDto)).thenReturn(TestUtils.copy(transaction));
        when(transactionMapper.toDto(transaction)).thenReturn(TestUtils.copy(infoDto));
        when(transactionRepository.findById(transaction.getId())).thenReturn(Optional.of(TestUtils.copy(transaction)));
        when(transactionRepository.save(transaction)).thenReturn(TestUtils.copy(transaction));

        TransactionInfoDto actual = underTest.update(infoDto);

        verify(transactionRepository, times(1)).save(transaction);

        assertThat(actual).isEqualTo(infoDto);
    }

    @Test
    @DisplayName("Should return true when invoke existsByExternalId")
    void shouldReturnTrue_when_invoke_existsByExternalId() {
        String externalId = "123";
        boolean expected = true;
        when(transactionRepository.existsByExternalId(externalId)).thenReturn(expected);
        boolean actual = underTest.existsByExternalId(externalId);

        // does not work
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should return true when invoke existsByExternalId")
    void should_get_ey_external_id() {
        Transaction transaction = TestUtils.transaction();
        TransactionInfoDto infoDto = TestUtils.getTransactionInfoDto();

        when(transactionRepository.findByExternalId(transaction.getExternalId()))
                .thenReturn(Optional.of(TestUtils.copy(transaction)));
        when(transactionMapper.toDto(transaction)).thenReturn(TestUtils.copy(infoDto));

        TransactionInfoDto actual = underTest.getByExternalId(transaction.getExternalId());

        assertThat(actual).isEqualTo(infoDto);
    }


    @Test
    void should_get_all_by_external_id_or_provider() {




    }
}
