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
   * Create a new FilterRegistrationBean instance fpr SvcProcessFilter.
   *
   * @param svcRegister      the service register.
   * @param svcProcessFilter the service process filter.
   * @return return the SvcProcessFilter for FilterRegistrationBean.
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
   * Create a new FilterRegistrationBean instance fpr SvcRequestBodyProcessFilter.
   *
   * @param svcRegister                 the service register.
   * @param svcRequestBodyProcessFilter the service request body process filter.
   * @return return the SvcRequestBodyProcessFilter for FilterRegistrationBean.
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
   * Create a new FilterRegistrationBean instance fpr SvcIpSecureFilter.
   *
   * @param svcRegister       the service register.
   * @param svcIpSecureFilter the service ip address secure filter.
   * @return return the SvcIpSecureFilter for FilterRegistrationBean.
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
   * Create a new FilterRegistrationBean instance fpr SvcRequestVerifyFilter.
   *
   * @param svcRegister            the service register.
   * @param svcRequestVerifyFilter the service request verify filter.
   * @return return the SvcRequestVerifyFilter for FilterRegistrationBean.
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
   * Create a new FilterRegistrationBean instance fpr SvcLogFilter.
   *
   * @param svcRegister  the service register.
   * @param svcLogFilter the service logging filter.
   * @return return the SvcLogFilter for FilterRegistrationBean.
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
   * Create a new FilterRegistrationBean instance fpr SvcProcessFilter.
   *
   * @param svcInfo                  the service information.
   * @param codeMessageHandler       the code message handler.
   * @param svcProperties            the service properties.
   * @param svcLoggingHandler        the service logging handler.
   * @param exceptionHandlerResolver the exception handler resolver.
   * @param svcRegister              the service register.
   * @param jacksonUtils             the jackson utils.
   * @param svcResUtils              the service response utils.
   * @return return the SvcProcessFilter for FilterRegistrationBean.
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
   * Create a new SvcRequestBodyProcessFilter instance.
   *
   * @param svcInfo                      the service information.
   * @param svcRequestBodyProcessHandler the service request body process handler.
   * @param jacksonUtils                 the jackson utils.
   * @return return the service request body process filter.
   */
  @Bean
  public SvcRequestBodyProcessFilter svcRequestBodyProcessFilter(SvcInfo svcInfo,
      SvcRequestBodyProcessHandler svcRequestBodyProcessHandler, JacksonUtils jacksonUtils) {
    return new SvcRequestBodyProcessFilter(svcInfo, svcRequestBodyProcessHandler, jacksonUtils);
  }

  /**
   * Create a new SvcIpSecureFilter instance.
   *
   * @param svcInfo the service information.
   * @return return the service ip address secure filter.
   */
  @Bean
  public SvcIpSecureFilter svcIpSecureFilter(SvcInfo svcInfo) {
    return new SvcIpSecureFilter(svcInfo);
  }

  /**
   * Create a new SvcLogFilter instance.
   *
   * @param svcInfo                      the service information.
   * @param serialNumberGeneratorHandler the serial number generator handler.
   * @param svcLoggingHandler            the service logging handler.
   * @return return the service logging filter.
   */
  @Bean
  public SvcLogFilter svcLogFilter(SvcInfo svcInfo, SvcLoggingHandler svcLoggingHandler,
      SerialNumberGeneratorHandler serialNumberGeneratorHandler) {
    return new SvcLogFilter(svcInfo, svcLoggingHandler, serialNumberGeneratorHandler);
  }

  /**
   * Create a new SvcRequestVerifyFilter instance.
   *
   * @param svcInfo      the service information.
   * @param validator    the validator.
   * @param jacksonUtils the jackson utils.
   * @return return the service request model valid filter.
   */
  @Bean
  public SvcRequestVerifyFilter svcReqModelValidFilter(SvcInfo svcInfo, Validator validator,
      JacksonUtils jacksonUtils) {
    return new SvcRequestVerifyFilter(svcInfo, validator, jacksonUtils);
  }

}
