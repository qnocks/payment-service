package com.itransition.payment.core.exception.custom;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class ExternalException extends RuntimeException {

    @Builder.Default
    private String message = "Error occurred while external communication";

    @Builder.Default
    private HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
}
