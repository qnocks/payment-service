package com.itransition.payment.auth.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itransition.payment.core.exception.handler.ErrorResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    private final ObjectMapper mapper;

    @SneakyThrows
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) {

        val errorResponse = ErrorResponse.builder()
                .message(accessDeniedException.getMessage())
                .status(HttpStatus.FORBIDDEN.value())
                .error(HttpStatus.FORBIDDEN)
                .build();

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(mapper.writeValueAsString(errorResponse));
    }
}
