package com.itransition.payment.auth.config;

import com.itransition.payment.auth.security.jwt.JwtSecurityConfigurer;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final Logger LOGGER = LogManager.getLogger(SecurityConfiguration.class);
    private static final String AUTH_ENDPOINT = "/auth/**";
    private static final String ADMIN_ENDPOINT = "/admin/transactions/**";
    private final JwtSecurityConfigurer jwtSecurityConfigurer;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() {
        try {
            return super.authenticationManager();
        } catch (Exception e) {
            LOGGER.warn(e.getMessage());
            return null;
        }
    }

    @SneakyThrows
    @Override
    protected void configure(@NotNull HttpSecurity http) {
        http.cors()
                    .and()
                .httpBasic()
                    .disable()
                .csrf()
                    .disable()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                .authorizeRequests()
                    .antMatchers(AUTH_ENDPOINT).permitAll()
                    .antMatchers(ADMIN_ENDPOINT).authenticated()
                    .anyRequest().permitAll()
                    .and()
                .exceptionHandling()
                    .authenticationEntryPoint(authenticationEntryPoint)
                    .and()
                .apply(jwtSecurityConfigurer);
    }
}
