package com.itransition.payment.core.it.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.itransition.payment.core.AssertionsHelper;
import com.itransition.payment.core.TestDataProvider;
import com.itransition.payment.core.dto.AuthResponse;
import com.itransition.payment.core.it.AbstractIntegrationTest;
import com.itransition.payment.core.service.impl.SecurityServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SecurityServiceIT extends AbstractIntegrationTest {

    @Autowired
    private SecurityServiceImpl underTest;

    @Autowired
    private ObjectMapper mapper;

    private final int PORT = 7000;
    private final WireMockServer server = new WireMockServer(PORT);
    private final AuthResponse expected = TestDataProvider.getAuthResponse();

    @BeforeAll
    void setupServer() throws JsonProcessingException {
        server.start();
        WireMock.configureFor("localhost", PORT);
        WireMock.stubFor(WireMock.post("auth/token/").willReturn(
                ResponseDefinitionBuilder.responseDefinition()
                        .withBody(mapper.writeValueAsString(expected))
                        .withStatus(200)
        ));
    }

    @Test
    void shouldAuthorize() {
        var actual = underTest.authorize();
        AssertionsHelper.verifyFieldsEqualityActualExpected(actual, expected);
    }
}