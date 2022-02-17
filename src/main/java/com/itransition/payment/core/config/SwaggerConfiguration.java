package com.itransition.payment.core.config;

import java.util.List;
import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.itransition.payment"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                getMessage("swagger.config.title"),
                getMessage("swagger.config.description"),
                getMessage("swagger.config.version"),
                getMessage("swagger.config.terms-of-service-url"),
                new Contact(
                        getMessage("swagger.config.contact.name"),
                        getMessage("swagger.config.contact.url"),
                        getMessage("swagger.config.contact.email")),
                getMessage("swagger.config.licence"),
                getMessage("swagger.config.license-url"),
                List.of());
    }

    private String getMessage(String key) {
        return swaggerMessageSource().getMessage(key, null, Locale.getDefault());
    }

    @Bean
    public MessageSource swaggerMessageSource() {
        var bundleMessageSource = new ReloadableResourceBundleMessageSource();
        bundleMessageSource.setBasename("classpath:/messages/swagger/swagger");
        bundleMessageSource.setDefaultEncoding("UTF-8");
        return bundleMessageSource;
    }
}
