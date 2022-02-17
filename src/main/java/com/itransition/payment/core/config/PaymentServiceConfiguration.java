package com.itransition.payment.core.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableJpaAuditing
@EnableScheduling
public class PaymentServiceConfiguration {

    @Bean("mock-webclient")
    public WebClient mockWebClient() {
        return WebClient.create("http://localhost:8082/");
    }

    @Bean
    public MessageSource exceptionMessageSource() {
        var bundleMessageSource = new ReloadableResourceBundleMessageSource();
        bundleMessageSource.setBasename("classpath:/messages/exception/exception");
        bundleMessageSource.setDefaultEncoding("UTF-8");
        return bundleMessageSource;
    }
}
