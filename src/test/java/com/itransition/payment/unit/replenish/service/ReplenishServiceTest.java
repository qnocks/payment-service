package com.itransition.payment.unit.replenish.service;

import com.itransition.payment.TestDataProvider;
import com.itransition.payment.core.entity.ReplenishError;
import com.itransition.payment.core.repository.TransactionRepository;
import com.itransition.payment.core.type.ReplenishmentStatus;
import com.itransition.payment.notify.service.NotifyService;
import com.itransition.payment.replenish.repository.ReplenishErrorRepository;
import com.itransition.payment.replenish.service.ReplenishAttemptCalc;
import com.itransition.payment.replenish.service.impl.ReplenishServiceImpl;
import com.itransition.payment.transaction.service.TransactionService;
import java.util.Optional;
import lombok.val;
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

    @Test
    void shouldUpdateReplenishStatusToSuccessWhenNotifySuccess() {
        val replenishDto = TestDataProvider.getTransactionReplenishDto();

        when(transactionService.getReadyToReplenish()).thenReturn(replenishDto);
        when(notifyService.sendTransaction(replenishDto)).thenReturn(Mono.just(new ResponseEntity<>(HttpStatus.OK)));

        underTest.replenish();

        verify(transactionService, times(1))
                .updateReplenishStatus(replenishDto, ReplenishmentStatus.SUCCESS);
    }

    @Test
    void shouldSaveReplenishErrorAndSetReplenishAfterWhenNotifyFailed() {
        val exceptionMessage = "test";
        val replenishAfter = 10.0;
        val replenishDto = TestDataProvider.getTransactionReplenishDto();
        val transaction = TestDataProvider.getTransaction();

        when(transactionService.getReadyToReplenish()).thenReturn(replenishDto);
        when(notifyService.sendTransaction(replenishDto))
                .thenReturn(Mono.error(() -> new IllegalStateException(exceptionMessage)));
        when(attemptCalc.canAnotherTry()).thenReturn(true);
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
    void shouldUpdateReplenishStatusToFailedWhenNotifyFailedAndThresholdLimited() {
        val replenishDto = TestDataProvider.getTransactionReplenishDto();

        when(transactionService.getReadyToReplenish()).thenReturn(replenishDto);
        when(notifyService.sendTransaction(replenishDto))
                .thenReturn(Mono.error(IllegalStateException::new));
        when(attemptCalc.canAnotherTry()).thenReturn(false);

        underTest.replenish();

        verify(transactionService, times(1))
                .updateReplenishStatus(replenishDto, ReplenishmentStatus.FAILED);

    }
}
