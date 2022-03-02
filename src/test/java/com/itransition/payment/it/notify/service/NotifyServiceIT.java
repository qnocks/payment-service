package com.itransition.payment.it.notify.service;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.itransition.payment.TestDataProvider;
import com.itransition.payment.it.AbstractIntegrationTest;
import com.itransition.payment.notify.service.NotifyService;
import com.itransition.payment.security.service.SecurityService;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@WireMockTest(httpPort = 8082)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NotifyServiceIT extends AbstractIntegrationTest {

    @Autowired
    private NotifyService underTest;

    @MockBean
    private SecurityService securityService;

    @Test
    void shouldReturnSuccessStatusWhenApiCallSuccess() {
        WireMock.stubFor(WireMock.post("transaction/").willReturn(
                ResponseDefinitionBuilder.responseDefinition()
                        .withStatus(HttpStatus.OK.value())));

        val replenishDto = TestDataProvider.getTransactionReplenishDto();
        val authResponse = TestDataProvider.getAuthResponse();

        when(securityService.getAuthHeader())
                .thenReturn(authResponse.getTokenType() + " " + authResponse.getAccessToken());

        val actual = underTest.sendTransaction(replenishDto);

        actual.subscribe(responseEntity -> assertThat(responseEntity).isEqualTo(ResponseEntity.ok().build()));
    }

    @Test
    void shouldReturnInternalServerErrorWhenApiCallFailed() {
        WireMock.stubFor(WireMock.post("transaction/").willReturn(
                ResponseDefinitionBuilder.responseDefinition()
                        .withStatus(HttpStatus.OK.value())));

        val authResponse = TestDataProvider.getAuthResponse();
        val replenishDto = TestDataProvider.getTransactionReplenishDto();

        when(securityService.getAuthHeader()).thenReturn(authResponse.getAccessToken());

        val actual = underTest.sendTransaction(replenishDto);

        actual.subscribe(responseEntity ->
                assertThat(responseEntity).isEqualTo(ResponseEntity.internalServerError().build()));
    }
}
