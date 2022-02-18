package com.itransition.payment.core.exception.custom;

public class TransactionNotUniqueException extends RuntimeException {

    public TransactionNotUniqueException(String message) {
        super(message);
    }
}
