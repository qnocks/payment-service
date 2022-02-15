package com.itransition.payment.core.service.impl;

import com.itransition.payment.core.domain.ReplenishError;
import com.itransition.payment.core.domain.enums.ReplenishmentStatus;
import com.itransition.payment.core.dto.ReplenishResponse;
import com.itransition.payment.core.dto.TransactionReplenishDto;
import com.itransition.payment.core.repository.ReplenishErrorRepository;
import com.itransition.payment.core.service.NotifyService;
import com.itransition.payment.core.service.ReplenishService;
import com.itransition.payment.core.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReplenishServiceImpl implements ReplenishService {

    private final ReplenishErrorRepository replenishErrorRepository;
    private final TransactionService transactionService;
    private final NotifyService notifyService;

    @Value("${app.replenish.threshold}")
    private int threshold;

    private int failedCount = 0;

    @Scheduled(cron = "${app.replenish.cron}")
    @Override
    public void replenish() {
        transactionService.findCompletedNotReplenished().ifPresent(replenishDto ->
                notifyService
                        .sendTransaction(replenishDto)
                        .subscribe(response -> processResponse(response, replenishDto)));
    }

    private void processResponse(ReplenishResponse response, TransactionReplenishDto replenishDto) {
        if (response.isSuccess()) {
            setReplenishStatus(replenishDto, ReplenishmentStatus.SUCCESS);
        }
        else if (!response.isSuccess() && ++failedCount < threshold) {
            saveReplenishError(response, replenishDto);
            setReplenishAfter(replenishDto, Math.exp(failedCount));
        }
        else {
            setReplenishStatus(replenishDto, ReplenishmentStatus.FAILED);
            failedCount = 0;
        }
    }

    private void setReplenishStatus(TransactionReplenishDto replenishDto, ReplenishmentStatus status) {
        transactionService.setReplenishStatus(status, replenishDto);
    }

    private void saveReplenishError(ReplenishResponse response, TransactionReplenishDto replenishDto) {
        var transaction = transactionService.getById(Long.valueOf(replenishDto.getGateId()));
        replenishErrorRepository.save(ReplenishError.builder()
                .error(response.getError())
                .transaction(transaction)
                .build());
    }

    private void setReplenishAfter(TransactionReplenishDto replenishDto, double replenishAfter) {
        transactionService.setReplenishAfter(replenishDto, replenishAfter);
    }
}
