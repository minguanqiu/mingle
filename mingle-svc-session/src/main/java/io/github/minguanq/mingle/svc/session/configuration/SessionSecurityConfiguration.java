package io.github.minguanq.mingle.svc.session.configuration;

import io.github.minguanq.mingle.svc.component.SvcRegisterComponent;
import io.github.minguanq.mingle.svc.session.annotation.SvcSession;
import io.github.minguanq.mingle.svc.session.configuration.properties.SessionProperties;
import io.github.minguanq.mingle.svc.session.dao.SessionDao;
import io.github.minguanq.mingle.svc.session.filter.SvcAuthenticationFilter;
import io.github.minguanq.mingle.svc.session.handler.SessionTokenHandler;
import io.github.minguanq.mingle.svc.session.security.SessionAccessDeniedHandler;
import io.github.minguanq.mingle.svc.session.security.SessionAuthenticationEntryPoint;
import io.github.minguanq.mingle.svc.session.security.SessionAuthenticationProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;

/**
 * Session security configuration
 *
 * @author Ming
 */

@Configuration
public class SessionSecurityConfiguration {

    @Bean
    public SecurityFilterChain sessionSecurityFilterChain(HttpSecurity httpSecurity, SvcRegisterComponent svcRegisterComponent, SvcAuthenticationFilter svcAuthenticationFilter) throws Exception {
        ArrayList<SvcRegisterComponent.SvcDefinition> svcDefinitions = svcRegisterComponent.getSvcDefinition(svcBinderModel -> svcBinderModel.getSvcClass().getAnnotation(SvcSession.class) != null);
        httpSecurity
                .securityMatcher(svcDefinitions.stream().map(SvcRegisterComponent.SvcDefinition::getSvcPath).toList().toArray(new String[0]))
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .exceptionHandling(p -> {
                    p.authenticationEntryPoint(sessionAuthenticationEntryPoint());
                    p.accessDeniedHandler(sessionAccessDeniedHandler());
                })
                .sessionManagement(sessionStrategy -> sessionStrategy.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(svcAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authorizeRequests -> {
                    svcDefinitions.stream().filter(svcBinderModel -> svcBinderModel.getSvcClass().getAnnotation(SvcSession.class).authority()).forEach(svcBinderModel -> {
                        authorizeRequests.requestMatchers(svcBinderModel.getSvcPath()).hasAuthority(svcBinderModel.getSvcName());
                    });
                    authorizeRequests.anyRequest().authenticated();
                });
        return httpSecurity.build();
    }

    @Bean
    @ConditionalOnMissingBean
    public SessionAuthenticationEntryPoint sessionAuthenticationEntryPoint() {
        return new SessionAuthenticationEntryPoint();
    }

    @Bean
    @ConditionalOnMissingBean
    public SessionAccessDeniedHandler sessionAccessDeniedHandler() {
        return new SessionAccessDeniedHandler();
    }

    @Bean
    public SessionAuthenticationProvider sessionAuthenticationProvider() {
        return new SessionAuthenticationProvider();
    }

    @Bean
    public FilterRegistrationBean<SvcAuthenticationFilter> svcSecurityFilterRegistration(SvcAuthenticationFilter svcAuthenticationFilter) {
        FilterRegistrationBean<SvcAuthenticationFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(svcAuthenticationFilter);
        registration.setName("svcAuthenticationFilter");
        registration.setEnabled(false);
        return registration;
    }

    @Bean
    public SvcAuthenticationFilter svcAuthenticationFilter(SvcRegisterComponent svcRegisterComponent, SessionProperties sessionProperties, SessionDao sessionDao, SessionTokenHandler sessionTokenHandler) {
        return new SvcAuthenticationFilter(svcRegisterComponent, sessionProperties, sessionDao, sessionTokenHandler);
    }


}
