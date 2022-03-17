package com.itransition.payment.core.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itransition.payment.core.exception.handler.ErrorResponse;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    private final ObjectMapper mapper;

    @SneakyThrows
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(mapper.writeValueAsString(ErrorResponse.builder()
                    .message(e.getMessage())
                    .error(HttpStatus.BAD_REQUEST)
                    .timestamp(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))
                    .build()));
        }
    }
}
