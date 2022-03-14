package com.itransition.payment.auth.exception.custom;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class AuthException extends RuntimeException {

    @Builder.Default
    private final String message = "Error occurred while authentication";

    @Builder.Default
    private final HttpStatus status = HttpStatus.UNAUTHORIZED;
}
