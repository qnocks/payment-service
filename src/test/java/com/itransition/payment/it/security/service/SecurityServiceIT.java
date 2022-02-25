package com.itransition.payment.it.security.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.itransition.payment.TestDataProvider;
import com.itransition.payment.it.AbstractIntegrationTest;
import com.itransition.payment.security.dto.AuthResponse;
import com.itransition.payment.security.service.impl.SecurityServiceImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

@WireMockTest(httpPort = 8082)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SecurityServiceIT extends AbstractIntegrationTest {

    @Autowired
    private SecurityServiceImpl underTest;

    @Autowired
    private ObjectMapper mapper;

    private final AuthResponse expected = TestDataProvider.getAuthResponse();

    @SneakyThrows
    @Test
    void shouldAuthorize() {
        WireMock.stubFor(WireMock.post("/auth/token?grant_type=&client_secret=&client_id=")
                .willReturn(ResponseDefinitionBuilder.responseDefinition()
                        .withBody(mapper.writeValueAsString(expected))
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(HttpStatus.OK.value())));

        var actual = underTest.getAuthHeader();
        assertThat(actual).isEqualTo(expected.getTokenType() + " " + expected.getAccessToken());
    }
}
