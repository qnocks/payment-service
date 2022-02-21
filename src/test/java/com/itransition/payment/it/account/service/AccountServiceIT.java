package com.itransition.payment.it.account.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.itransition.payment.AssertionsHelper;
import com.itransition.payment.TestDataProvider;
import com.itransition.payment.account.dto.AccountDto;
import com.itransition.payment.it.AbstractIntegrationTest;
import com.itransition.payment.security.service.SecurityService;
import com.itransition.payment.account.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountServiceIT extends AbstractIntegrationTest {

    @Autowired
    private AccountServiceImpl underTest;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private SecurityService securityService;

    private final int PORT = 7000;
    private final WireMockServer server = new WireMockServer(PORT);
    private final AccountDto expected = TestDataProvider.getAccountDto();
    private final String ACCOUNT_ID = "1";

    @BeforeAll
    void setupServer() throws JsonProcessingException {
        server.start();
        WireMock.configureFor("localhost", PORT);
        WireMock.stubFor(WireMock.post("/account/" + ACCOUNT_ID).willReturn(
                ResponseDefinitionBuilder.responseDefinition()
                        .withBody(mapper.writeValueAsString(expected))
                        .withStatus(200)
        ));
    }

    @AfterAll
    void tearDown() {
        if (server.isRunning()) {
            server.shutdownServer();
        }
    }

    @Test
    void shouldGetById() {
        var authResponse = TestDataProvider.getAuthResponse();
        when(securityService.getAuthHeader())
                .thenReturn(authResponse.getTokenType() + " " + authResponse.getAccessToken());

        var actual = underTest.getById(ACCOUNT_ID);
        AssertionsHelper.verifyFieldsEqualityActualExpected(actual, expected);
    }
}
