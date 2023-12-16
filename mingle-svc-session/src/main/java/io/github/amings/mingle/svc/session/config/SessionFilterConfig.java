package io.github.amings.mingle.svc.session.config;

import io.github.amings.mingle.svc.config.properties.SvcProperties;
import io.github.amings.mingle.svc.filter.SvcInfo;
import io.github.amings.mingle.svc.handler.SvcMsgHandler;
import io.github.amings.mingle.svc.session.component.SessionBinderComponent;
import io.github.amings.mingle.svc.session.filter.JwtAuthenticationFilter;
import io.github.amings.mingle.svc.session.utils.SessionUtils;
import io.github.amings.mingle.utils.JacksonUtils;
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
    public FilterRegistrationBean<JwtAuthenticationFilter> svcSecurityFilterRegistration(JwtAuthenticationFilter jwtAuthenticationFilter) {
        FilterRegistrationBean<JwtAuthenticationFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(jwtAuthenticationFilter);
        registration.setName("jwtAuthenticationFilter");
        registration.setEnabled(false);
        return registration;
    }

    @Bean
    @Primary
    public JwtAuthenticationFilter jwtAuthenticationFilter(SvcInfo svcInfo, SvcMsgHandler svcMsgHandler, SvcProperties svcProperties, SessionUtils sessionUtils, JacksonUtils jacksonUtils) {
        return new JwtAuthenticationFilter(svcInfo, svcMsgHandler, svcProperties, sessionUtils, jacksonUtils);
    }

}
