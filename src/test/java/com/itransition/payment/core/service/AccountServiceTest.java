package com.itransition.payment.core.service;

import com.itransition.payment.core.TestDataProvider;
import com.itransition.payment.core.dto.AuthResponse;
import com.itransition.payment.core.exception.ExceptionMessageResolver;
import com.itransition.payment.core.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @InjectMocks
    private AccountServiceImpl underTest;

    @Mock
    private WebClient webClient;

    @Mock
    private SecurityService securityService;

    @Mock
    private ExceptionMessageResolver exceptionMessageResolver;

    @Disabled
    @Test
    void shouldGetById() {
        String accountId = "123";
        var authResponse = TestDataProvider.getAuthResponse();

        when(securityService.authorize("", "", "")).thenReturn(authResponse);

        // TODO: Mock webclient chain call or within wiremock

        var actual = underTest.getById(accountId);
    }
}
