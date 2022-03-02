package com.itransition.payment.core.exception;

import com.itransition.payment.account.service.AccountService;
import com.itransition.payment.core.exception.custom.ExternalException;
import com.itransition.payment.core.exception.custom.TransactionException;
import com.itransition.payment.security.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientRequestException;

@Component
@RequiredArgsConstructor
public class ExceptionHelper {

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

    public ExternalException handleExternalException(Throwable e, Class<?> clazz, String... params) {
        if (clazz.equals(AccountService.class)) {
            return processAccountExternalException((Exception) e, params);
        } else if (clazz.equals(SecurityService.class)) {
            return processSecurityExternalException((Exception) e);
        }

        return ExternalException.builder().build();
    }

    private ExternalException processAccountExternalException(Exception e, String... params) {
        if (e instanceof WebClientRequestException) {
            return buildExternalException("account.service-not-available");
        }

        return buildExternalException(HttpStatus.BAD_REQUEST, "account.cannot-get", params[0]);
    }

    private ExternalException processSecurityExternalException(Exception e) {
        if (e instanceof WebClientRequestException) {
            return buildExternalException("security.service-not-available");
        }

        return buildExternalException("security.auth-error");
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
