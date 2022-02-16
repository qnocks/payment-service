package com.itransition.payment.core.unit.service;

import com.itransition.payment.core.TestDataProvider;
import com.itransition.payment.core.domain.ReplenishError;
import com.itransition.payment.core.domain.enums.ReplenishmentStatus;
import com.itransition.payment.core.exception.ExceptionMessageResolver;
import com.itransition.payment.core.repository.ReplenishErrorRepository;
import com.itransition.payment.core.repository.TransactionRepository;
import com.itransition.payment.core.service.NotifyService;
import com.itransition.payment.core.service.ReplenishAttemptCalc;
import com.itransition.payment.core.service.TransactionService;
import com.itransition.payment.core.service.impl.ReplenishServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReplenishServiceTest {

    @InjectMocks
    private ReplenishServiceImpl underTest;

    @Mock
    private ReplenishErrorRepository replenishErrorRepository;

    @Mock
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private NotifyService notifyService;

    @Mock
    private ReplenishAttemptCalc attemptCalc;

    @Mock
    private ExceptionMessageResolver messageResolver;

    @Test
    void shouldUpdateReplenishStatusToSuccess_when_notifySuccess() {
        var replenishDto = TestDataProvider.getTransactionReplenishDto();

        when(transactionService.findReadyToReplenish()).thenReturn(Optional.of(replenishDto));
        when(notifyService.sendTransaction(replenishDto)).thenReturn(Mono.just(new ResponseEntity<>(HttpStatus.OK)));

        underTest.replenish();

        verify(transactionService, times(1))
                .updateReplenishStatus(replenishDto, ReplenishmentStatus.SUCCESS);
    }

    @Test
    void shouldSaveReplenishErrorAndSetReplenishAfter_when_notifyFailed() {
        var exceptionMessage = "test";
        var replenishAfter = 10.0;
        var replenishDto = TestDataProvider.getTransactionReplenishDto();
        var transaction = TestDataProvider.getTransaction();

        when(transactionService.findReadyToReplenish()).thenReturn(Optional.of(replenishDto));
        when(notifyService.sendTransaction(replenishDto))
                .thenReturn(Mono.error(() -> new IllegalStateException(exceptionMessage)));
        when(attemptCalc.canTryReplenish()).thenReturn(true);
        when(attemptCalc.calcNextAttemptTime()).thenReturn(replenishAfter);
        when(transactionRepository.findById(Long.valueOf(replenishDto.getGateId())))
                .thenReturn(Optional.of(transaction));

        underTest.replenish();

        verify(transactionRepository, times(1)).findById(Long.valueOf(replenishDto.getGateId()));
        verify(replenishErrorRepository, times(1)).save(ReplenishError.builder()
                .error(exceptionMessage)
                .transaction(transaction)
                .build());
        verify(transactionService, times(1)).setReplenishAfter(replenishDto, replenishAfter);
    }

    @Test
    void shouldUpdateReplenishStatusToFailed_when_notifyFailedAndThresholdLimited() {
        var exceptionMessage = "test";
        var replenishDto = TestDataProvider.getTransactionReplenishDto();

        when(transactionService.findReadyToReplenish()).thenReturn(Optional.of(replenishDto));
        when(notifyService.sendTransaction(replenishDto))
                .thenReturn(Mono.error(() -> new IllegalStateException(exceptionMessage)));
        when(attemptCalc.canTryReplenish()).thenReturn(false);

        underTest.replenish();

        verify(transactionService, times(1))
                .updateReplenishStatus(replenishDto, ReplenishmentStatus.FAILED);

    }
}
