package io.github.minguanqiu.mingle.svc.configuration;

import io.github.minguanqiu.mingle.svc.configuration.properties.SvcProperties;
import io.github.minguanqiu.mingle.svc.exception.handler.resolver.ExceptionHandlerResolver;
import io.github.minguanqiu.mingle.svc.filter.SvcInfo;
import io.github.minguanqiu.mingle.svc.filter.SvcIpSecureFilter;
import io.github.minguanqiu.mingle.svc.filter.SvcLogFilter;
import io.github.minguanqiu.mingle.svc.filter.SvcProcessFilter;
import io.github.minguanqiu.mingle.svc.filter.SvcRequestBodyProcessFilter;
import io.github.minguanqiu.mingle.svc.filter.SvcRequestVerifyFilter;
import io.github.minguanqiu.mingle.svc.handler.CodeMessageHandler;
import io.github.minguanqiu.mingle.svc.handler.SerialNumberGeneratorHandler;
import io.github.minguanqiu.mingle.svc.handler.SvcLoggingHandler;
import io.github.minguanqiu.mingle.svc.handler.SvcRequestBodyProcessHandler;
import io.github.minguanqiu.mingle.svc.handler.model.SvcFeature;
import io.github.minguanqiu.mingle.svc.register.SvcRegister;
import io.github.minguanqiu.mingle.svc.utils.JacksonUtils;
import io.github.minguanqiu.mingle.svc.utils.SvcResUtils;
import jakarta.validation.Validator;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for filter register.
 *
 * @author Qiu Guan Ming
 */
@Configuration
public class FilterConfiguration {

  /**
   * Create and return {@link FilterRegistrationBean} for {@link SvcProcessFilter}.
   */
  @Bean
  public FilterRegistrationBean<SvcProcessFilter> svcPreProcessFilterRegistrationBean(
      SvcRegister svcRegister, SvcProcessFilter svcProcessFilter) {
    FilterRegistrationBean<SvcProcessFilter> registration = new FilterRegistrationBean<>();
    registration.setFilter(svcProcessFilter);
    registration.setName("svcPreProcessFilter");
    registration.addUrlPatterns(svcRegister.getSvcPath(svcDefinition -> true));
    registration.setOrder(0);
    return registration;
  }

  /**
   * Create and return {@link FilterRegistrationBean} for {@link SvcRequestBodyProcessFilter}.
   */
  @Bean
  public FilterRegistrationBean<SvcRequestBodyProcessFilter> svcRequestBodyProcessFilterRegistrationBean(
      SvcRegister svcRegister, SvcRequestBodyProcessFilter svcRequestBodyProcessFilter) {
    FilterRegistrationBean<SvcRequestBodyProcessFilter> registration = new FilterRegistrationBean<>();
    registration.setFilter(svcRequestBodyProcessFilter);
    registration.setName("svcRequestBodyProcessFilter");
    registration.addUrlPatterns(svcRegister.getSvcPath(svcDefinition -> true));
    registration.setOrder(1);
    return registration;
  }

  /**
   * Create and return {@link FilterRegistrationBean} for {@link SvcIpSecureFilter}.
   */
  @Bean
  public FilterRegistrationBean<SvcIpSecureFilter> svcIpSecureFilterRegistrationBean(
      SvcRegister svcRegister, SvcIpSecureFilter svcIpSecureFilter) {
    FilterRegistrationBean<SvcIpSecureFilter> registration = new FilterRegistrationBean<>();
    registration.setFilter(svcIpSecureFilter);
    registration.setName("svcIpSecureFilter");
    String[] svcPath = svcRegister.getSvcPath(
        svcDefinition -> svcDefinition.getFeature(SvcFeature.class).get().ip_secure().length > 0);
    if (svcPath.length == 0) {
      registration.setEnabled(false);
    } else {
      registration.addUrlPatterns(svcPath);
    }
    registration.setOrder(5);
    return registration;
  }

  /**
   * Create and return {@link FilterRegistrationBean} for {@link SvcRequestVerifyFilter}.
   */
  @Bean
  public FilterRegistrationBean<SvcRequestVerifyFilter> svcRequestVerifyFilterRegistrationBean(
      SvcRegister svcRegister, SvcRequestVerifyFilter svcRequestVerifyFilter) {
    FilterRegistrationBean<SvcRequestVerifyFilter> registration = new FilterRegistrationBean<>();
    registration.setFilter(svcRequestVerifyFilter);
    registration.setName("svcReqModelValidFilter");
    String[] svcPath = svcRegister.getSvcPath(svcDefinition -> true);
    if (svcPath.length == 0) {
      registration.setEnabled(false);
    } else {
      registration.addUrlPatterns(svcPath);
    }
    registration.setOrder(10);
    return registration;
  }

  /**
   * Create and return {@link FilterRegistrationBean} for {@link SvcLogFilter}.
   */
  @Bean
  public FilterRegistrationBean<SvcLogFilter> svcLogFilterRegistrationBean(SvcRegister svcRegister,
      SvcLogFilter svcLogFilter) {
    FilterRegistrationBean<SvcLogFilter> registration = new FilterRegistrationBean<>();
    registration.setFilter(svcLogFilter);
    registration.setName("svcLogFilter");
    String[] svcPath = svcRegister.getSvcPath(
        svcDefinition -> svcDefinition.getFeature(SvcFeature.class).get().logging());
    if (svcPath.length == 0) {
      registration.setEnabled(false);
    } else {
      registration.addUrlPatterns(svcPath);
    }
    registration.setOrder(15);
    return registration;
  }

  /**
   * Create and return instance.
   */
  @Bean
  public SvcProcessFilter svcPreProcessFilter(SvcInfo svcInfo,
      CodeMessageHandler codeMessageHandler, SvcProperties svcProperties,
      SvcLoggingHandler svcLoggingHandler, ExceptionHandlerResolver exceptionHandlerResolver,
      SvcRegister svcRegister, JacksonUtils jacksonUtils, SvcResUtils svcResUtils) {
    return new SvcProcessFilter(svcInfo, codeMessageHandler, svcProperties, svcLoggingHandler,
        exceptionHandlerResolver, svcRegister, jacksonUtils, svcResUtils);
  }

  /**
   * Create and return instance.
   */
  @Bean
  public SvcRequestBodyProcessFilter svcRequestBodyProcessFilter(SvcInfo svcInfo,
      SvcRequestBodyProcessHandler svcRequestBodyProcessHandler, JacksonUtils jacksonUtils) {
    return new SvcRequestBodyProcessFilter(svcInfo, svcRequestBodyProcessHandler, jacksonUtils);
  }

  /**
   * Create and return instance.
   */
  @Bean
  public SvcIpSecureFilter svcIpSecureFilter(SvcInfo svcInfo) {
    return new SvcIpSecureFilter(svcInfo);
  }

  /**
   * Create and return instance.
   */
  @Bean
  public SvcLogFilter svcLogFilter(SvcInfo svcInfo, SvcLoggingHandler svcLoggingHandler,
      SerialNumberGeneratorHandler serialNumberGeneratorHandler) {
    return new SvcLogFilter(svcInfo, svcLoggingHandler, serialNumberGeneratorHandler);
  }

  /**
   * Create and return instance.
   */
  @Bean
  public SvcRequestVerifyFilter svcReqModelValidFilter(SvcInfo svcInfo, Validator validator,
      JacksonUtils jacksonUtils) {
    return new SvcRequestVerifyFilter(svcInfo, validator, jacksonUtils);
  }

}
