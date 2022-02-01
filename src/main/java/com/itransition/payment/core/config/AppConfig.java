package com.itransition.payment.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

    @Bean("mock-webclient")
    public WebClient mockWebClient() {
        return WebClient.create("http://localhost:8081/mock/");
    }
}
