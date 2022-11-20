package io.github.amings.mingle.svc.session.config;

import io.github.amings.mingle.svc.session.component.SessionBinderComponent;
import io.github.amings.mingle.svc.session.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Session filter configuration
 *
 * @author Ming
 */

@Configuration
public class SessionFilterConfig {
    @Autowired
    SessionBinderComponent sessionBinderComponent;

    @Bean
    @Primary
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> svcSecurityFilterRegistration() {
        FilterRegistrationBean<JwtAuthenticationFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(jwtAuthenticationFilter());
        registration.setName("jwtAuthenticationFilter");
        registration.setEnabled(false);
        return registration;
    }

}
