package com.itransition.payment.replenish.service.impl;

import com.itransition.payment.replenish.service.ReplenishAttemptCalc;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ReplenishAttemptCalcImpl implements ReplenishAttemptCalc {

    @Value("${app.replenish.threshold}")
    private int threshold;
    private int failedCount = 0;

    @Override
    public boolean canAnotherTry() {
        val isCannotTryToReplenish = ++failedCount > threshold;
        if (isCannotTryToReplenish) {
            failedCount = 0;
        }

        return !isCannotTryToReplenish;
    }

    @Override
    public double calcNextAttemptTime() {
        return Math.exp(failedCount);
    }
}
