package io.github.amings.mingle.svc.session.config;

import io.github.amings.mingle.svc.filter.SvcPreProcessFilter;
import io.github.amings.mingle.svc.session.component.SessionBinderComponent;
import io.github.amings.mingle.svc.session.filter.JwtAuthenticationFilter;
import io.github.amings.mingle.svc.session.security.SessionAccessDeniedHandler;
import io.github.amings.mingle.svc.session.security.SessionAuthenticationEntryPoint;
import io.github.amings.mingle.svc.session.security.SessionAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Session security configuration
 *
 * @author Ming
 */

@Configuration
public class SessionSecurityConfig {

    SessionBinderComponent sessionBinderComponent;
    JwtAuthenticationFilter jwtAuthenticationFilter;
    SvcPreProcessFilter svcPreProcessFilter;
    SessionAuthenticationProvider sessionAuthenticationProvider;
    SessionAuthenticationEntryPoint sessionAuthenticationEntryPoint;
    SessionAccessDeniedHandler sessionAccessDeniedHandler;

    public SessionSecurityConfig(SessionBinderComponent sessionBinderComponent, JwtAuthenticationFilter jwtAuthenticationFilter, SvcPreProcessFilter svcPreProcessFilter, SessionAuthenticationProvider sessionAuthenticationProvider, SessionAuthenticationEntryPoint sessionAuthenticationEntryPoint, SessionAccessDeniedHandler sessionAccessDeniedHandler) {
        this.sessionBinderComponent = sessionBinderComponent;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.svcPreProcessFilter = svcPreProcessFilter;
        this.sessionAuthenticationProvider = sessionAuthenticationProvider;
        this.sessionAuthenticationEntryPoint = sessionAuthenticationEntryPoint;
        this.sessionAccessDeniedHandler = sessionAccessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain sessionSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .securityMatcher(sessionBinderComponent.getSessionPathList().toArray(new String[0]))
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .exceptionHandling(p -> {
                    p.authenticationEntryPoint(sessionAuthenticationEntryPoint);
                    p.accessDeniedHandler(sessionAccessDeniedHandler);
                })
                .sessionManagement(sessionStrategy -> sessionStrategy.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(svcPreProcessFilter, JwtAuthenticationFilter.class)
                .authenticationProvider(sessionAuthenticationProvider)
                .authorizeHttpRequests(authorizeRequests -> {
                    sessionBinderComponent.getAuthorityMap().forEach((k, v) -> {
                        authorizeRequests.requestMatchers(k).hasAuthority(v);
                    });
                    authorizeRequests.anyRequest().authenticated();
                });
        return httpSecurity.build();
    }

}
