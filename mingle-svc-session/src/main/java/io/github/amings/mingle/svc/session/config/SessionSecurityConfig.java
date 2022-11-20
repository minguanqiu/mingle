package io.github.amings.mingle.svc.session.config;

import io.github.amings.mingle.svc.session.component.SessionBinderComponent;
import io.github.amings.mingle.svc.session.filter.JwtAuthenticationFilter;
import io.github.amings.mingle.svc.session.security.SessionAccessDeniedHandler;
import io.github.amings.mingle.svc.session.security.SessionAuthenticationEntryPoint;
import io.github.amings.mingle.svc.session.security.SessionAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
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
    @Autowired
    SessionBinderComponent sessionBinderComponent;
    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    SessionAuthenticationProvider sessionAuthenticationProvider;
    @Autowired
    SessionAuthenticationEntryPoint sessionAuthenticationEntryPoint;
    @Autowired
    SessionAccessDeniedHandler sessionAccessDeniedHandler;

    @Bean
    @Order(0)
    public SecurityFilterChain sessionSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .requestMatchers().antMatchers(sessionBinderComponent.getSessionPathList().toArray(new String[0]))
                .and()
                .csrf()
                .disable()
                .httpBasic()
                .disable()
                .formLogin()
                .disable()
                .exceptionHandling(p -> {
                    p.authenticationEntryPoint(sessionAuthenticationEntryPoint);
                    p.accessDeniedHandler(sessionAccessDeniedHandler);
                })
                .sessionManagement(sessionStrategy -> sessionStrategy.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(sessionAuthenticationProvider)
                .authorizeRequests(authorizeRequests -> {
                    sessionBinderComponent.getAuthorityMap().forEach((k, v) -> {
                        authorizeRequests.antMatchers(k).hasAuthority(v);
                    });
                    authorizeRequests.anyRequest().authenticated();
                });
        return httpSecurity.build();
    }

}
