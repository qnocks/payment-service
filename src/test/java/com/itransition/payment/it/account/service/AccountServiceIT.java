package com.itransition.payment.it.account.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.itransition.payment.AssertionsHelper;
import com.itransition.payment.TestDataProvider;
import com.itransition.payment.account.service.impl.AccountServiceImpl;
import com.itransition.payment.it.AbstractIntegrationTest;
import com.itransition.payment.security.service.SecurityService;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.mockito.Mockito.when;

@WireMockTest(httpPort = 8082)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountServiceIT extends AbstractIntegrationTest {

    @Autowired
    private AccountServiceImpl underTest;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private SecurityService securityService;

    @SneakyThrows
    @Test
    void shouldGetById() {
        val accountId = "1";
        val expected = TestDataProvider.getAccountDto();

        WireMock.stubFor(WireMock.get("/account/" + accountId).willReturn(
                ResponseDefinitionBuilder.responseDefinition()
                        .withBody(mapper.writeValueAsString(expected))
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(HttpStatus.OK.value())));

        var authResponse = TestDataProvider.getAuthResponse();
        when(securityService.getAuthHeader())
                .thenReturn(authResponse.getTokenType() + " " + authResponse.getAccessToken());

        var actual = underTest.getById(accountId);
        AssertionsHelper.verifyFieldsEqualityActualExpected(actual, expected);
    }
}
