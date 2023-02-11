package io.github.amings.mingle.svc.config;

import io.github.amings.mingle.svc.component.SvcBinderComponent;
import io.github.amings.mingle.svc.security.SvcAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration
 *
 * @author Ming
 */

@Configuration
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
public class SvcSecurityConfig {

    @Autowired
    SvcBinderComponent svcBinderComponent;
    @Autowired
    SvcAuthenticationEntryPoint svcAuthenticationEntryPoint;
    @Value("${springdoc.api-docs.path:/v3/api-docs}")
    private String apiDocPath;
    @Value("${springdoc.swagger-ui.path:/swagger-ui.html}")
    private String swaggerUIPath;
    @Value("${mingle.svc.security.ip.openapi:}")
    private String openapiAddress;

    @Bean
    @Order
    public SecurityFilterChain svcSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .requestMatchers()
                .antMatchers(svcBinderComponent.getSvcPathList().toArray(new String[0]))
                .antMatchers(apiDocPath)
                .antMatchers(apiDocPath + "/*")
                .antMatchers("/swagger-ui/*")
                .and()
                .csrf()
                .disable()
                .httpBasic()
                .disable()
                .formLogin()
                .disable()
                .exceptionHandling(p -> {
                    p.authenticationEntryPoint(svcAuthenticationEntryPoint);
                })
                .authorizeRequests(authorizeRequests -> {
                    svcBinderComponent.getIpAddressMap().forEach((k, v) -> {
                        authorizeRequests.antMatchers(k).access(v);
                    });
                    if(!openapiAddress.equals("")) {
                        authorizeRequests.antMatchers(apiDocPath).access(svcBinderComponent.buildIpAddressPattern(openapiAddress));
                        authorizeRequests.antMatchers(apiDocPath + "/*").access(svcBinderComponent.buildIpAddressPattern(openapiAddress));
                        authorizeRequests.antMatchers("/swagger-ui/*").access(svcBinderComponent.buildIpAddressPattern(openapiAddress));
                    }
                    authorizeRequests.anyRequest().permitAll();
                });
        return httpSecurity.build();
    }

}
