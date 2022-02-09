package com.itransition.payment.core.service;

import com.itransition.payment.core.domain.Transaction;
import com.itransition.payment.core.domain.enums.TransactionStatus;
import com.itransition.payment.core.dto.TransactionAdapterStateDto;
import com.itransition.payment.core.dto.TransactionInfoDto;
import com.itransition.payment.core.exception.ExceptionMessageResolver;
import com.itransition.payment.core.mapper.TransactionMapper;
import com.itransition.payment.core.repository.TransactionRepository;
import com.itransition.payment.core.service.impl.TransactionServiceImpl;
import com.itransition.payment.core.TransactionTestUtils;
import java.util.List;
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
class TransactionServiceImplTest {

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
        TransactionAdapterStateDto stateDto = TransactionTestUtils.transactionAdapterStateDto();
        Transaction transaction = TransactionTestUtils.transactionAdapterStateDtoToTransaction();
        TransactionInfoDto expected = TransactionTestUtils.transactionToTransactionInfoDto();

        given(transactionMapper.toEntity(stateDto)).willReturn(transaction);
        given(transactionMapper.toDto(transaction)).willReturn(TransactionTestUtils.copy(expected));
        when(paymentProviderService.getByProvider(stateDto.getProvider())).thenReturn(transaction.getProvider());

        TransactionInfoDto actual = underTest.save(stateDto);

        verify(transactionRepository, times(1)).saveAndFlush(transaction);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should update Transaction")
    void shouldUpdateTransaction() {
        Transaction transaction = TransactionTestUtils.transactionInfoDtoToTransaction();
        transaction.setStatus(TransactionStatus.COMPLETED);
        Transaction existingTransaction = TransactionTestUtils.transaction();
        TransactionInfoDto expected = TransactionTestUtils.transactionToTransactionInfoDto();
        expected.setStatus(TransactionStatus.COMPLETED);

        when(transactionMapper.toEntity(expected)).thenReturn(TransactionTestUtils.copy(transaction));
        when(paymentProviderService.getByProvider(transaction.getProvider().getName()))
                .thenReturn(transaction.getProvider());
        when(transactionRepository.findById(transaction.getId()))
                .thenReturn(Optional.of(existingTransaction));
        when(transactionMapper.toDto(existingTransaction)).thenReturn(TransactionTestUtils.copy(expected));

        TransactionInfoDto actual = underTest.update(expected);

        verify(transactionRepository, times(1)).findById(transaction.getId());
        verify(transactionRepository, times(1)).save(existingTransaction);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should return true when invoke existsByExternalId")
    void shouldReturnTrue_when_existsByExternalId() {
        String externalId = "123";
        when(transactionRepository.existsByExternalId(externalId)).thenReturn(true);
        boolean actual = underTest.existsByExternalId(externalId);
        assertThat(actual).isEqualTo(true);
    }

    @Test
    @DisplayName("Should get  by externalId")
    void shouldGetByExternalId() {
        Transaction transaction = TransactionTestUtils.transaction();
        TransactionInfoDto expected = TransactionTestUtils.transactionInfoDto();

        when(transactionRepository.findByExternalId(transaction.getExternalId()))
                .thenReturn(Optional.of(TransactionTestUtils.copy(transaction)));
        when(transactionMapper.toDto(transaction)).thenReturn(TransactionTestUtils.copy(expected));

        TransactionInfoDto actual = underTest.getByExternalId(transaction.getExternalId());

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should get all by externalId and provider")
    void shouldGetAllByExternalIdOrProvider() {
        TransactionInfoDto infoDto = TransactionTestUtils.transactionInfoDto();
        Transaction transaction = TransactionTestUtils.transaction();

        when(transactionRepository.findAllByExternalIdAndProviderName(infoDto.getExternalId(), infoDto.getProvider()))
                .thenReturn(List.of(transaction));
        when(transactionMapper.toDto(transaction)).thenReturn(TransactionTestUtils.transactionInfoDto());

        List<TransactionInfoDto> actual = underTest
                .getAllByExternalIdOrProvider(infoDto.getExternalId(), infoDto.getProvider());

        assertThat(actual.size()).isEqualTo(1);
        assertThat(actual.get(0)).isEqualTo(infoDto);
    }
}
