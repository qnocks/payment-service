package com.itransition.payment.core.exception.custom;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
@EqualsAndHashCode(callSuper = true)
public class TransactionException extends RuntimeException {

    @Builder.Default
    private final String message = "Error occurred while transaction processing";

    @Builder.Default
    private final HttpStatus status = HttpStatus.BAD_REQUEST;
}
