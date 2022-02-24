package com.itransition.payment.it.account.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.itransition.payment.AssertionsHelper;
import com.itransition.payment.TestDataProvider;
import com.itransition.payment.account.dto.AccountDto;
import com.itransition.payment.account.service.impl.AccountServiceImpl;
import com.itransition.payment.it.AbstractIntegrationTest;
import com.itransition.payment.security.service.SecurityService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${test.api.port}")
    private int port;
    private WireMockServer server;
    private final AccountDto expected = TestDataProvider.getAccountDto();
    private final String accountId = "1";

    @BeforeAll
    void setupServer() throws JsonProcessingException {
        server = new WireMockServer(port);
        server.start();
        WireMock.configureFor("localhost", port);
        WireMock.stubFor(WireMock.get("/account/" + accountId).willReturn(
                ResponseDefinitionBuilder.responseDefinition()
                        .withBody(mapper.writeValueAsString(expected))
                        .withHeader("Content-type", "application/json")
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

        var actual = underTest.getById(accountId);
        AssertionsHelper.verifyFieldsEqualityActualExpected(actual, expected);
    }
}
