package com.itransition.payment.replenish.service.impl;

import com.itransition.payment.core.entity.ReplenishError;
import com.itransition.payment.core.exception.custom.TransactionNotFoundException;
import com.itransition.payment.core.types.ReplenishmentStatus;
import com.itransition.payment.core.dto.TransactionReplenishDto;
import com.itransition.payment.core.exception.ExceptionMessageResolver;
import com.itransition.payment.replenish.repository.ReplenishErrorRepository;
import com.itransition.payment.core.repository.TransactionRepository;
import com.itransition.payment.notify.service.NotifyService;
import com.itransition.payment.replenish.service.ReplenishAttemptCalc;
import com.itransition.payment.replenish.service.ReplenishService;
import com.itransition.payment.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReplenishServiceImpl implements ReplenishService {

    private final ReplenishErrorRepository replenishErrorRepository;
    private final TransactionService transactionService;
    private final TransactionRepository transactionRepository;
    private final NotifyService notifyService;
    private final ReplenishAttemptCalc attemptCalc;
    private final ExceptionMessageResolver exceptionMessages;

    @Scheduled(cron = "${app.replenish.cron}")
    @Override
    public void replenish() {
        var replenishDto = transactionService.getReadyToReplenish();

        if (replenishDto != null) {
            notifyService.sendTransaction(replenishDto).subscribe(
                    response -> successCallback(replenishDto),
                    error -> failureCallback(replenishDto, error.getMessage()));
        }
    }

    private void successCallback(TransactionReplenishDto replenishDto) {
        updateReplenishStatus(replenishDto, ReplenishmentStatus.SUCCESS);
    }

    private void failureCallback(TransactionReplenishDto replenishDto, String error) {
        boolean canTryToReplenish = attemptCalc.canAnotherTry();
        if (canTryToReplenish) {
            saveReplenishError(error, replenishDto);
            setReplenishAfter(replenishDto, attemptCalc.calcNextAttemptTime());
        }
        else {
            updateReplenishStatus(replenishDto, ReplenishmentStatus.FAILED);
        }
    }

    private void updateReplenishStatus(TransactionReplenishDto replenishDto, ReplenishmentStatus status) {
        transactionService.updateReplenishStatus(replenishDto, status);
    }

    private void saveReplenishError(String error, TransactionReplenishDto replenishDto) {
        // TODO: Should be changed to custom exception when implementation of exception handling
        var transaction = transactionRepository.findById(Long.valueOf(replenishDto.getGateId()))
                .orElseThrow(() -> new TransactionNotFoundException(
                        exceptionMessages.getMessage("transaction.cannot-get-by-id", replenishDto.getGateId())));

        replenishErrorRepository.save(ReplenishError.builder()
                .error(error)
                .transaction(transaction)
                .build());
    }

    private void setReplenishAfter(TransactionReplenishDto replenishDto, double replenishAfter) {
        transactionService.setReplenishAfter(replenishDto, replenishAfter);
    }
}
