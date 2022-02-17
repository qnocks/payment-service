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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

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
        var actual = underTest.getAuthHeader();
        assertThat(actual).isEqualTo(expected.getTokenType() + " " + expected.getAccessToken());
    }
}
