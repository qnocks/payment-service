package com.itransition.payment.core.config;

import java.util.List;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsPolicyConfiguration {

    @Value("${app.cors.origins}")
    private String corsOrigins;
    private static final String AUTH_ENDPOINT = "/auth/**";
    private static final String ADMIN_ENDPOINT = "/admin/transactions/**";

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        val configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(parseCsvOrigins());
        configuration.setAllowedMethods(List.of(
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PUT.name()));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(List.of(
                HttpHeaders.AUTHORIZATION,
                HttpHeaders.CACHE_CONTROL,
                HttpHeaders.CONTENT_TYPE));

        val source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(AUTH_ENDPOINT, configuration);
        source.registerCorsConfiguration(ADMIN_ENDPOINT, configuration);

        return source;
    }

    private List<String> parseCsvOrigins() {
        return List.of(corsOrigins.split("\\s*,\\s*"));
    }
}
