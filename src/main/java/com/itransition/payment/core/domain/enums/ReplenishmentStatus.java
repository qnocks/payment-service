package com.itransition.payment.core.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReplenishmentStatus {

    // TODO: Should be clarified according to specification
    INITIAL("initial"),
    COMPLETED("completed"),
    FAILED("failed");

    private final String name;
}
