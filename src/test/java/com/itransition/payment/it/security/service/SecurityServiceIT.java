package com.itransition.payment.it.security.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.itransition.payment.TestDataProvider;
import com.itransition.payment.it.AbstractIntegrationTest;
import com.itransition.payment.security.dto.AuthResponse;
import com.itransition.payment.security.service.impl.SecurityServiceImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SecurityServiceIT extends AbstractIntegrationTest {

    @Autowired
    private SecurityServiceImpl underTest;

    @Autowired
    private ObjectMapper mapper;

    @Value("${test.api.port}")
    private int port;
    private WireMockServer server;
    private final AuthResponse expected = TestDataProvider.getAuthResponse();

    @BeforeAll
    void setupServer() throws JsonProcessingException {
        server = new WireMockServer(port);
        server.start();
        WireMock.configureFor("localhost", port);
        WireMock.stubFor(WireMock.post("/auth/token?grant_type=&client_secret=&client_id=")
                .willReturn(ResponseDefinitionBuilder.responseDefinition()
                        .withBody(mapper.writeValueAsString(expected))
                        .withHeader("Content-type", "application/json")
                        .withStatus(200)));
    }

    @AfterAll
    void tearDown() {
        if (server.isRunning()) {
            server.shutdownServer();
        }
    }

    @Test
    void shouldAuthorize() {
        var actual = underTest.getAuthHeader();
        assertThat(actual).isEqualTo(expected.getTokenType() + " " + expected.getAccessToken());
    }
}
