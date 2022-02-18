package com.itransition.payment.core.exception.handler;

import com.itransition.payment.core.exception.custom.AccountAbsenceException;
import com.itransition.payment.core.exception.custom.TransactionNotFoundException;
import com.itransition.payment.core.exception.custom.TransactionNotUniqueException;
import com.itransition.payment.core.exception.custom.TransactionStatusCannotBeChangedException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class CustomExceptionHandler {

    // TODO: Fix HttpStatus responses

    public static final String TRACE = "trace";

    @Value("${app.exception.trace}")
    private boolean printStackTrace;

    @ExceptionHandler(AccountAbsenceException.class)
    public ResponseEntity<CustomErrorResponse> handleAccountAbsenceException(
            AccountAbsenceException e, WebRequest request) {
        return buildResponse(e, e.getMessage(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(TransactionNotUniqueException.class)
    public ResponseEntity<CustomErrorResponse> handleTransactionNotUniqueException(
            TransactionNotUniqueException e, WebRequest request) {
        return buildResponse(e, e.getMessage(),  HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(TransactionStatusCannotBeChangedException.class)
    public ResponseEntity<CustomErrorResponse> handleTransactionStatusCannotBeChangedException(
            TransactionStatusCannotBeChangedException e, WebRequest request) {
        return buildResponse(e, e.getMessage(), HttpStatus.BAD_GATEWAY, request);
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleTransactionNotFoundException(
            TransactionNotFoundException e, WebRequest request) {
        return buildResponse(e, e.getMessage(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> handlerUnknownException(Exception e, WebRequest request) {
        return buildResponse(e, "Unknown error occurred", HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private ResponseEntity<CustomErrorResponse> buildResponse(
            Exception e, String message, HttpStatus status, WebRequest request) {
        var response = CustomErrorResponse.builder()
                .message(message)
                .status(status.value())
                .error(status)
                .timestamp(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))
                .build();

        if (printStackTrace && isTraceOn(request)) {
            response.setStackTrace(ExceptionUtils.getStackTrace(e));
        }

        return ResponseEntity.status(status).body(response);
    }

    private boolean isTraceOn(WebRequest request) {
        var value = request.getParameterValues(TRACE);
        return Objects.nonNull(value)
                && value.length > 0
                && value[0].contentEquals("true");
    }
}
