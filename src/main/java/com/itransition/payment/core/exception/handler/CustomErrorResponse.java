package com.itransition.payment.core.exception.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomErrorResponse {

    private final String message;
    private final Integer status;
    private final HttpStatus error;
    private String stackTrace;
    private final Long timestamp;
}
