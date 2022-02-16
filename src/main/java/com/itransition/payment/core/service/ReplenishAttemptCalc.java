package com.itransition.payment.core.service;

public interface ReplenishAttemptCalc {

    boolean canTryReplenish();

    double calcNextAttemptTime();
}
