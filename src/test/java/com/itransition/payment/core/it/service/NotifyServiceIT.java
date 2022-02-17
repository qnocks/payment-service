package com.itransition.payment.core.it.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.itransition.payment.core.TestDataProvider;
import com.itransition.payment.core.it.AbstractIntegrationTest;
import com.itransition.payment.core.service.NotifyService;
import com.itransition.payment.core.service.SecurityService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final int PORT = 7000;
    private final WireMockServer server = new WireMockServer(PORT);

    @BeforeAll
    void setupServer() {
        server.start();
        WireMock.configureFor("localhost", PORT);

    }

    @AfterAll
    void tearDown() {
        if (server.isRunning()) {
            server.shutdownServer();
        }
    }

    @Test
    void shouldReturnSuccessStatus_when_ApiCallSuccess() {
        WireMock.stubFor(WireMock.post("transaction/").willReturn(
                ResponseDefinitionBuilder.responseDefinition()
                        .withStatus(200)));

        var replenishDto = TestDataProvider.getTransactionReplenishDto();
        var authResponse = TestDataProvider.getAuthResponse();

        when(securityService.authorize()).thenReturn(authResponse);

        var actual = underTest.sendTransaction(replenishDto);

        actual.subscribe(responseEntity -> assertThat(responseEntity).isEqualTo(ResponseEntity.ok().build()));
    }
    
    @Test
    void shouldReturnInternalServerError_when_ApiCallFailed() {
        var errorMessage = "test";
        WireMock.stubFor(WireMock.post("transaction/").willReturn(
                ResponseDefinitionBuilder.responseDefinition()
                        .withStatus(500)
                        .withStatusMessage(errorMessage)));

        var replenishDto = TestDataProvider.getTransactionReplenishDto();
        var authResponse = TestDataProvider.getAuthResponse();

        when(securityService.authorize()).thenReturn(authResponse);

        var actual = underTest.sendTransaction(replenishDto);

        actual.subscribe(responseEntity ->
                assertThat(responseEntity).isEqualTo(ResponseEntity.internalServerError().build()));
    }
}
