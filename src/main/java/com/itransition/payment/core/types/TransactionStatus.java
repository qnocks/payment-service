package com.itransition.payment.core.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransactionStatus {

    INITIAL("initial"),
    COMPLETED("completed"),
    FAILED("failed");

    private final String name;
}
