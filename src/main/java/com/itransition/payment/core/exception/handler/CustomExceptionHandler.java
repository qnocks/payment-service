package com.itransition.payment.core.exception.handler;

import com.itransition.payment.core.exception.custom.AccountException;
import com.itransition.payment.core.exception.custom.ExternalAuthException;
import com.itransition.payment.core.exception.custom.TransactionException;
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

    public static final String TRACE = "trace";

    @Value("${app.exception.trace}")
    private boolean printStackTrace;

    @ExceptionHandler(AccountException.class)
    public ResponseEntity<ErrorResponse> handleAccountAbsenceException(
            AccountException e, WebRequest request) {
        return buildResponse(e, e.getMessage(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(TransactionException.class)
    public ResponseEntity<ErrorResponse> handleTransactionException(TransactionException e, WebRequest request) {
        return buildResponse(e, e.getMessage(), e.getStatus(), request);
    }

    @ExceptionHandler(ExternalAuthException.class)
    public ResponseEntity<ErrorResponse> handleExternalAuthException(
            ExternalAuthException e, WebRequest request) {
        return buildResponse(e, e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handlerUnknownException(Exception e, WebRequest request) {
        return buildResponse(e, "Unknown error occurred", HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private ResponseEntity<ErrorResponse> buildResponse(
            Exception e, String message, HttpStatus status, WebRequest request) {
        var response = ErrorResponse.builder()
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
