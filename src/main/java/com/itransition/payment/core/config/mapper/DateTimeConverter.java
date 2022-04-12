package com.itransition.payment.core.config.mapper;

import com.itransition.payment.core.util.DateTimeUtils;
import java.time.LocalDateTime;
import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;

public class DateTimeConverter extends CustomConverter<LocalDateTime, Long> {

    @Override
    public Long convert(LocalDateTime source, Type<? extends Long> destinationType, MappingContext mappingContext) {
        return DateTimeUtils.toSeconds(source);
    }
}
