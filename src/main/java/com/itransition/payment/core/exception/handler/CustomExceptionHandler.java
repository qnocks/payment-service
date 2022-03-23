package com.itransition.payment.core.exception.handler;

import com.itransition.payment.core.exception.custom.ExternalException;
import com.itransition.payment.core.exception.custom.TransactionException;
import com.itransition.payment.core.util.DateTimeUtils;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import lombok.val;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class CustomExceptionHandler {

    public static final String TRACE = "trace";

    @Value("${app.exception.trace}")
    private boolean printStackTrace;

    @ExceptionHandler(TransactionException.class)
    public ErrorResponse handleTransactionException(@NotNull TransactionException e, WebRequest request) {
        return buildResponse(e, e.getMessage(), e.getStatus(), request);
    }

    @ExceptionHandler(ExternalException.class)
    public ErrorResponse handleExternalAuthException(@NotNull ExternalException e, WebRequest request) {
        return buildResponse(e, e.getMessage(), e.getStatus(), request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ErrorResponse handleAccessDeniedException(@NotNull AccessDeniedException e, WebRequest request) {
        return buildResponse(e, e.getMessage(), HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ErrorResponse handleAuthException(@NotNull BadCredentialsException e, WebRequest request) {
        return buildResponse(e, e.getMessage(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(Exception.class)
    public ErrorResponse handleUnknownException(Exception e, WebRequest request) {
        return buildResponse(e, "Unknown error occurred", HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private ErrorResponse buildResponse(Exception e, String message, HttpStatus status, WebRequest request) {
        val response = ErrorResponse.builder()
                .message(message)
                .status(status.value())
                .error(status)
                .timestamp(DateTimeUtils.toSeconds(LocalDateTime.now()))
                .build();

        if (printStackTrace && isTraceOn(request)) {
            response.setStackTrace(ExceptionUtils.getStackTrace(e));
        }

        return response;
    }

    private boolean isTraceOn(@NotNull WebRequest request) {
        val value = request.getParameterValues(TRACE);
        return Objects.nonNull(value)
                && value.length > 0
                && value[0].contentEquals("true");
    }
}
