package com.itransition.payment.core.exception.custom;

public class TransactionStatusCannotBeChangedException extends RuntimeException{

    public TransactionStatusCannotBeChangedException(String message) {
        super(message);
    }
}
