package com.itransition.payment.core.service;

public interface ReplenishAttemptCalc {

    boolean canAnotherTry();

    double calcNextAttemptTime();
}
