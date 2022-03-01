package com.itransition.payment.replenish.service.impl;

import com.itransition.payment.core.dto.TransactionReplenishDto;
import com.itransition.payment.core.entity.ReplenishError;
import com.itransition.payment.core.exception.ExceptionHelper;
import com.itransition.payment.core.exception.custom.ExternalException;
import com.itransition.payment.core.repository.TransactionRepository;
import com.itransition.payment.core.type.ReplenishmentStatus;
import com.itransition.payment.notify.service.NotifyService;
import com.itransition.payment.replenish.repository.ReplenishErrorRepository;
import com.itransition.payment.replenish.service.ReplenishAttemptCalc;
import com.itransition.payment.replenish.service.ReplenishService;
import com.itransition.payment.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.HttpStatus;
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
    private final ExceptionHelper exceptionHelper;

    @Scheduled(cron = "${app.replenish.cron}")
    @Override
    public void replenish() {
        val replenishDto = transactionService.getReadyToReplenish();

        if (replenishDto != null) {
            handleSendTransaction(replenishDto);
        }
    }

    private void handleSendTransaction(TransactionReplenishDto replenishDto) {
        try {
            notifyService.sendTransaction(replenishDto).subscribe(
                    response -> successCallback(replenishDto),
                    error -> failureCallback(replenishDto, error.getMessage()));
        } catch (ExternalException e) {
            failureCallback(replenishDto, e.getMessage());
        }
    }

    private void successCallback(TransactionReplenishDto replenishDto) {
        updateReplenishStatus(replenishDto, ReplenishmentStatus.SUCCESS);
    }

    private void failureCallback(TransactionReplenishDto replenishDto, String error) {
        if (attemptCalc.canAnotherTry()) {
            saveReplenishError(error, replenishDto);
            setReplenishAfter(replenishDto, attemptCalc.calcNextAttemptTime());
        } else {
            updateReplenishStatus(replenishDto, ReplenishmentStatus.FAILED);
        }
    }

    private void updateReplenishStatus(TransactionReplenishDto replenishDto, ReplenishmentStatus status) {
        transactionService.updateReplenishStatus(replenishDto, status);
    }

    private void saveReplenishError(String error, TransactionReplenishDto replenishDto) {
        replenishErrorRepository.save(ReplenishError.builder()
                .error(error)
                .transaction(transactionRepository
                        .findById(Long.valueOf(replenishDto.getGateId()))
                        .orElseThrow(() -> exceptionHelper.buildTransactionException(
                                HttpStatus.INTERNAL_SERVER_ERROR, "transaction.cannot-get-by-id")))
                .build());
    }

    private void setReplenishAfter(TransactionReplenishDto replenishDto, double replenishAfter) {
        transactionService.setReplenishAfter(replenishDto, replenishAfter);
    }
}
