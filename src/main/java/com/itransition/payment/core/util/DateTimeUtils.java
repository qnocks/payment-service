package com.itransition.payment.core.util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateTimeUtils {

    public static Date toDate(@NotNull LocalDateTime value) {
        return Date.from(value.toInstant(ZoneOffset.UTC));
    }

    public static long toSeconds(@NotNull LocalDateTime value) {
        return value.toEpochSecond(ZoneOffset.UTC);
    }
}
