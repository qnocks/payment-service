package com.itransition.payment.core.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI().info(new Info()
                .title("Payment Service")
                .version("1.0")
                .description("Application which accepts payments from a wide amount of difference payment provider " +
                        "providers and broadcasts the result of successfully finished payment transactions")
                .contact(new Contact().name("Roman Ostanin").email("r.ostanin@itransition.com")));
    }
}
