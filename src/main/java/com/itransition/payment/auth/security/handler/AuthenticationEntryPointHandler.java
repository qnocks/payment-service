package com.itransition.payment.auth.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itransition.payment.core.exception.handler.ErrorResponse;
import com.itransition.payment.core.util.DateTimeUtils;
import java.time.LocalDateTime;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationEntryPointHandler implements AuthenticationEntryPoint {

    private final ObjectMapper mapper;

    @SneakyThrows
    @Override
    public void commence(HttpServletRequest request,
                         @NotNull HttpServletResponse response,
                         @NotNull AuthenticationException authException) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(mapper.writeValueAsString(ErrorResponse.builder()
                .message(authException.getMessage())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(HttpStatus.UNAUTHORIZED)
                .timestamp(DateTimeUtils.toSeconds(LocalDateTime.now()))
                .build()));
    }
}
