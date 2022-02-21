package com.itransition.payment.core.exception.custom;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class ExternalAuthException extends RuntimeException {

    private String message;
}
