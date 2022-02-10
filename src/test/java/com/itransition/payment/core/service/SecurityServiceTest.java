package com.itransition.payment.core.service;

import com.itransition.payment.core.service.impl.SecurityServiceImpl;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

@ExtendWith(MockitoExtension.class)
class SecurityServiceTest {

    @InjectMocks
    private SecurityServiceImpl underTest;

    @Mock
    private WebClient webClient;

    @Disabled
    @Test
    void shouldAuthorize() {
        // TODO: Mock webclient chain call or within wiremock
    }
}
