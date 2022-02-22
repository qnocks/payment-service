package com.itransition.payment.core.exception.custom;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class TransactionException extends RuntimeException {

    @Builder.Default
    private String message = "Error occurred while transaction processing";

    @Builder.Default
    private HttpStatus status = HttpStatus.BAD_REQUEST;
}
