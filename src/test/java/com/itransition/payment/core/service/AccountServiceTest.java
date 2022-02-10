package com.itransition.payment.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.itransition.payment.core.AbstractIntegrationTest;
import com.itransition.payment.core.AssertionsHelper;
import com.itransition.payment.core.TestDataProvider;
import com.itransition.payment.core.dto.AccountDto;
import com.itransition.payment.core.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountServiceTest extends AbstractIntegrationTest {

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
        when(securityService.authorize("", "", "")).thenReturn(authResponse);
        var actual = underTest.getById(ACCOUNT_ID);
        AssertionsHelper.verifyFieldsEqualityActualExpected(actual, expected);
    }
}
