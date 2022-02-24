package com.itransition.payment.it.notify.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.itransition.payment.TestDataProvider;
import com.itransition.payment.it.AbstractIntegrationTest;
import com.itransition.payment.notify.service.NotifyService;
import com.itransition.payment.security.service.SecurityService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NotifyServiceIT extends AbstractIntegrationTest {

    @Autowired
    private NotifyService underTest;

    @MockBean
    private SecurityService securityService;

    @Value("${test.api.port}")
    private int port;
    private WireMockServer server;

    @BeforeAll
    void setupServer() {
        server = new WireMockServer(port);
        server.start();
        WireMock.configureFor("localhost", port);
    }

    @AfterAll
    void tearDown() {
        if (server.isRunning()) {
            server.shutdownServer();
        }
    }

    @Test
    void shouldReturnSuccessStatusWhenApiCallSuccess() {
        WireMock.stubFor(WireMock.post("transaction/").willReturn(
                ResponseDefinitionBuilder.responseDefinition()
                        .withStatus(200)));

        var replenishDto = TestDataProvider.getTransactionReplenishDto();
        var authResponse = TestDataProvider.getAuthResponse();

        when(securityService.getAuthHeader())
                .thenReturn(authResponse.getTokenType() + " " + authResponse.getAccessToken());

        var actual = underTest.sendTransaction(replenishDto);

        actual.subscribe(responseEntity -> assertThat(responseEntity).isEqualTo(ResponseEntity.ok().build()));
    }

    @Test
    void shouldReturnInternalServerErrorWhenApiCallFailed() {
        var errorMessage = "test";
        WireMock.stubFor(WireMock.post("transaction/").willReturn(
                ResponseDefinitionBuilder.responseDefinition()
                        .withStatus(500)
                        .withStatusMessage(errorMessage)));

        var replenishDto = TestDataProvider.getTransactionReplenishDto();
        var authResponse = TestDataProvider.getAuthResponse();

        when(securityService.getAuthHeader()).thenReturn(authResponse.getAccessToken());

        var actual = underTest.sendTransaction(replenishDto);

        actual.subscribe(responseEntity ->
                assertThat(responseEntity).isEqualTo(ResponseEntity.internalServerError().build()));
    }
}
