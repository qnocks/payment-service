package com.itransition.payment.core.exception;

import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExceptionMessageResolver {

    private final MessageSource exceptionMessageSource;

    public String getMessage(String key, Object... params) {
        return exceptionMessageSource.getMessage(key, params, Locale.getDefault());
    }
}
