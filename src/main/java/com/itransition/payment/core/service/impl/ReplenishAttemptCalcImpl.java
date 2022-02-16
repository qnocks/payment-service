package com.itransition.payment.core.service.impl;

import com.itransition.payment.core.service.ReplenishAttemptCalc;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ReplenishAttemptCalcImpl implements ReplenishAttemptCalc {

    @Value("${app.replenish.threshold}")
    private int threshold;
    private int failedCount = 0;

    @Override
    public boolean canTryReplenish() {
        boolean canTryToReplenish = ++failedCount <= threshold;
        if (!canTryToReplenish) {
            failedCount = 0;
        }

        return canTryToReplenish;
    }

    @Override
    public double calcNextAttemptTime() {
        return Math.exp(failedCount);
    }
}
