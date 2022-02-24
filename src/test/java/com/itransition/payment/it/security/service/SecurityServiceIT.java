package com.itransition.payment.it.security.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.itransition.payment.TestDataProvider;
import com.itransition.payment.it.AbstractIntegrationTest;
import com.itransition.payment.security.dto.AuthResponse;
import com.itransition.payment.security.service.impl.SecurityServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@WireMockTest(httpPort = 8082)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SecurityServiceIT extends AbstractIntegrationTest {

    @Autowired
    private SecurityServiceImpl underTest;

    @Autowired
    private ObjectMapper mapper;

    private final AuthResponse expected = TestDataProvider.getAuthResponse();

    @Test
    void shouldAuthorize() throws JsonProcessingException {
        WireMock.stubFor(WireMock.post("/auth/token?grant_type=&client_secret=&client_id=")
                .willReturn(ResponseDefinitionBuilder.responseDefinition()
                        .withBody(mapper.writeValueAsString(expected))
                        .withHeader("Content-type", "application/json")
                        .withStatus(200)));

        var actual = underTest.getAuthHeader();
        assertThat(actual).isEqualTo(expected.getTokenType() + " " + expected.getAccessToken());
    }
}
