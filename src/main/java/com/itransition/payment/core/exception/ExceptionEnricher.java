package com.itransition.payment.core.exception;

import com.itransition.payment.core.exception.custom.ExternalException;
import com.itransition.payment.core.exception.custom.TransactionException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExceptionEnricher {

    private final ExceptionMessageResolver exceptionMessageResolver;

    public TransactionException buildTransactionException(String messageKey, Object... params) {
        return TransactionException.builder()
                .message(exceptionMessageResolver.getMessage(messageKey, params))
                .build();
    }

    public TransactionException buildTransactionException(HttpStatus status, String messageKey, Object... params) {
        return TransactionException.builder()
                .message(exceptionMessageResolver.getMessage(messageKey, params))
                .status(status)
                .build();
    }

    public ExternalException buildExternalException(String messageKey, Object... params) {
        return ExternalException.builder()
                .message(exceptionMessageResolver.getMessage(messageKey, params))
                .build();
    }

    public ExternalException buildExternalException(HttpStatus status, String messageKey, Object... params) {
        return ExternalException.builder()
                .message(exceptionMessageResolver.getMessage(messageKey, params))
                .status(status)
                .build();
    }
}
