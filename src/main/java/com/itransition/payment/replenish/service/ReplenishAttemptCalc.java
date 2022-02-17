package com.itransition.payment.replenish.service;

public interface ReplenishAttemptCalc {

    boolean canAnotherTry();

    double calcNextAttemptTime();
}
