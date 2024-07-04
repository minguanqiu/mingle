package io.github.minguanqiu.mingle.svc.session.configuration;

import io.github.minguanqiu.mingle.svc.filter.SvcProcessFilter;
import io.github.minguanqiu.mingle.svc.register.SvcDefinition;
import io.github.minguanqiu.mingle.svc.register.SvcRegister;
import io.github.minguanqiu.mingle.svc.session.configuration.properties.SvcSessionProperties;
import io.github.minguanqiu.mingle.svc.session.dao.SvcSessionDao;
import io.github.minguanqiu.mingle.svc.session.filter.SvcAuthenticationFilter;
import io.github.minguanqiu.mingle.svc.session.handler.SessionTokenHandler;
import io.github.minguanqiu.mingle.svc.session.handler.model.SvcSessionFeature;
import io.github.minguanqiu.mingle.svc.session.security.SessionAccessDeniedHandler;
import io.github.minguanqiu.mingle.svc.session.security.SessionAuthenticationEntryPoint;
import io.github.minguanqiu.mingle.svc.session.security.SessionAuthenticationProvider;
import io.github.minguanqiu.mingle.svc.utils.JacksonUtils;
import java.util.ArrayList;
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

/**
 * Configuration for session security.
 *
 * @author Qiu Guan Ming
 */
@Configuration
public class SessionSecurityConfiguration {

  /**
   * Spring security filter chain for session.
   *
   * @param httpSecurity            the httpSecurity.
   * @param svcRegister             the service register.
   * @param svcProcessFilter        the service process filter.
   * @param svcAuthenticationFilter the service authentication filter.
   * @return return the filter chain.
   * @throws Exception when build error.
   */
  @Bean
  public SecurityFilterChain sessionSecurityFilterChain(HttpSecurity httpSecurity,
      SvcRegister svcRegister, SvcProcessFilter svcProcessFilter,
      SvcAuthenticationFilter svcAuthenticationFilter) throws Exception {
    ArrayList<SvcDefinition> svcDefinitions = svcRegister.getSvcDefinition(
        svcDefinition -> svcDefinition.getFeature(SvcSessionFeature.class).isPresent()
            && svcDefinition.getFeature(SvcSessionFeature.class).get().types().length != 0);
    httpSecurity
        .securityMatcher(svcDefinitions.stream().map(SvcDefinition::getSvcPath).toList()
            .toArray(new String[0]))
        .csrf(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .exceptionHandling(p -> {
          p.authenticationEntryPoint(sessionAuthenticationEntryPoint());
          p.accessDeniedHandler(sessionAccessDeniedHandler());
        })
        .sessionManagement(sessionStrategy -> sessionStrategy.sessionCreationPolicy(
            SessionCreationPolicy.STATELESS))
        .addFilterBefore(svcAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(svcProcessFilter, SvcAuthenticationFilter.class)
        .authorizeHttpRequests(authorizeRequests -> {
          svcDefinitions.stream().filter(
                  svcDefinition -> svcDefinition.getFeature(SvcSessionFeature.class).get().authority())
              .forEach(svcBinderModel -> {
                authorizeRequests.requestMatchers(svcBinderModel.getSvcPath())
                    .hasAuthority(svcBinderModel.getSvcName());
              });
          authorizeRequests.anyRequest().authenticated();
        });
    return httpSecurity.build();
  }

  /**
   * Create SessionAuthenticationEntryPoint instance.
   *
   * @return return the SessionAuthenticationEntryPoint.
   */
  @Bean
  @ConditionalOnMissingBean
  public SessionAuthenticationEntryPoint sessionAuthenticationEntryPoint() {
    return new SessionAuthenticationEntryPoint();
  }

  /**
   * Create SessionAccessDeniedHandler instance.
   *
   * @return return the SessionAccessDeniedHandler.
   */
  @Bean
  @ConditionalOnMissingBean
  public SessionAccessDeniedHandler sessionAccessDeniedHandler() {
    return new SessionAccessDeniedHandler();
  }

  /**
   * Create SessionAuthenticationProvider instance.
   *
   * @return return the SessionAuthenticationProvider.
   */
  @Bean
  public SessionAuthenticationProvider sessionAuthenticationProvider() {
    return new SessionAuthenticationProvider();
  }

  /**
   * Create FilterRegistrationBean instance for SvcAuthenticationFilter.
   *
   * @param svcAuthenticationFilter the service authentication filter.
   * @return return the FilterRegistrationBean instance for SvcAuthenticationFilter.
   */
  @Bean
  public FilterRegistrationBean<SvcAuthenticationFilter> svcSecurityFilterRegistration(
      SvcAuthenticationFilter svcAuthenticationFilter) {
    FilterRegistrationBean<SvcAuthenticationFilter> registration = new FilterRegistrationBean<>();
    registration.setFilter(svcAuthenticationFilter);
    registration.setName("svcAuthenticationFilter");
    registration.setEnabled(false);
    return registration;
  }

  /**
   * Create SvcAuthenticationFilter instance.
   *
   * @param svcRegister          the service register.
   * @param svcSessionProperties the service session properties.
   * @param svcSessionDao        the service session DAO.
   * @param sessionTokenHandler  the session token handler.
   * @param jacksonUtils         the jackson utils.
   * @return return the SvcAuthenticationFilter.
   */
  @Bean
  public SvcAuthenticationFilter svcAuthenticationFilter(SvcRegister svcRegister,
      SvcSessionProperties svcSessionProperties, SvcSessionDao svcSessionDao,
      SessionTokenHandler sessionTokenHandler,
      @Qualifier("sessionJacksonUtils") JacksonUtils jacksonUtils) {
    return new SvcAuthenticationFilter(svcRegister, svcSessionProperties, svcSessionDao,
        sessionTokenHandler, jacksonUtils);
  }


}
