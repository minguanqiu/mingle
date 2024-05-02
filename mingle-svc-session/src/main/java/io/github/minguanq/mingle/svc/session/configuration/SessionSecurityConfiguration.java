package io.github.minguanq.mingle.svc.session.configuration;

import io.github.minguanq.mingle.svc.filter.SvcProcessFilter;
import io.github.minguanq.mingle.svc.register.SvcRegister;
import io.github.minguanq.mingle.svc.session.configuration.properties.SvcSessionProperties;
import io.github.minguanq.mingle.svc.session.dao.SessionDao;
import io.github.minguanq.mingle.svc.session.filter.SvcAuthenticationFilter;
import io.github.minguanq.mingle.svc.session.handler.SessionTokenHandler;
import io.github.minguanq.mingle.svc.session.handler.model.SvcSessionFeature;
import io.github.minguanq.mingle.svc.session.security.SessionAccessDeniedHandler;
import io.github.minguanq.mingle.svc.session.security.SessionAuthenticationEntryPoint;
import io.github.minguanq.mingle.svc.session.security.SessionAuthenticationProvider;
import io.github.minguanq.mingle.svc.utils.JacksonUtils;
import org.springframework.beans.factory.annotation.Qualifier;
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
    public SecurityFilterChain sessionSecurityFilterChain(HttpSecurity httpSecurity, SvcRegister svcRegister, SvcProcessFilter svcProcessFilter, SvcAuthenticationFilter svcAuthenticationFilter) throws Exception {
        ArrayList<SvcRegister.SvcDefinition> svcDefinitions = svcRegister.getSvcDefinition(svcDefinition -> svcDefinition.getFeature(SvcSessionFeature.class).isPresent() && svcDefinition.getFeature(SvcSessionFeature.class).get().types().length != 0);
        httpSecurity
                .securityMatcher(svcDefinitions.stream().map(SvcRegister.SvcDefinition::getSvcPath).toList().toArray(new String[0]))
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .exceptionHandling(p -> {
                    p.authenticationEntryPoint(sessionAuthenticationEntryPoint());
                    p.accessDeniedHandler(sessionAccessDeniedHandler());
                })
                .sessionManagement(sessionStrategy -> sessionStrategy.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(svcAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(svcProcessFilter, SvcAuthenticationFilter.class)
                .authorizeHttpRequests(authorizeRequests -> {
                    svcDefinitions.stream().filter(svcDefinition -> svcDefinition.getFeature(SvcSessionFeature.class).get().authority()).forEach(svcBinderModel -> {
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
    public SvcAuthenticationFilter svcAuthenticationFilter(SvcRegister svcRegister, SvcSessionProperties svcSessionProperties, SessionDao sessionDao, SessionTokenHandler sessionTokenHandler, @Qualifier("sessionJacksonUtils") JacksonUtils jacksonUtils) {
        return new SvcAuthenticationFilter(svcRegister, svcSessionProperties, sessionDao, sessionTokenHandler, jacksonUtils);
    }


}
