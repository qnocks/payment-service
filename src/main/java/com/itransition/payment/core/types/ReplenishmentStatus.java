package com.itransition.payment.core.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReplenishmentStatus {

    INITIAL("initial"),
    SUCCESS("success"),
    FAILED("failed");

    private final String name;
}
