package com.itransition.payment.core.domain.enums;

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
