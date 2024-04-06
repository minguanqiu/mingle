package io.github.minguanq.mingle.svc.configuration;

import io.github.minguanq.mingle.svc.component.SvcRegisterComponent;
import io.github.minguanq.mingle.svc.configuration.properties.SvcProperties;
import io.github.minguanq.mingle.svc.exception.handler.resolver.ExceptionHandlerResolver;
import io.github.minguanq.mingle.svc.filter.*;
import io.github.minguanq.mingle.svc.handler.SerialNumberGeneratorHandler;
import io.github.minguanq.mingle.svc.handler.SvcLoggingHandler;
import io.github.minguanq.mingle.svc.handler.SvcMsgHandler;
import io.github.minguanq.mingle.svc.handler.SvcRequestBodyProcessHandler;
import io.github.minguanq.mingle.svc.utils.JacksonUtils;
import io.github.minguanq.mingle.svc.utils.SvcResUtils;
import jakarta.validation.Validator;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for filter register
 *
 * @author Ming
 */
@Configuration
public class FilterConfiguration {

    /**
     * Create and return {@link FilterRegistrationBean} for {@link SvcProcessFilter}
     */
    @Bean
    public FilterRegistrationBean<SvcProcessFilter> svcPreProcessFilterFilterRegistrationBean(SvcRegisterComponent svcRegisterComponent, SvcProcessFilter svcProcessFilter) {
        FilterRegistrationBean<SvcProcessFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(svcProcessFilter);
        registration.setName("svcPreProcessFilter");
        registration.addUrlPatterns(svcRegisterComponent.getSvcPath(svcDefinition -> true));
        registration.setOrder(0);
        return registration;
    }

    /**
     * Create and return {@link FilterRegistrationBean} for {@link SvcRequestBodyProcessFilter}
     */
    @Bean
    public FilterRegistrationBean<SvcRequestBodyProcessFilter> svcRequestBodyProcessFilterFilterRegistrationBean(SvcRegisterComponent svcRegisterComponent, SvcRequestBodyProcessFilter svcRequestBodyProcessFilter) {
        FilterRegistrationBean<SvcRequestBodyProcessFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(svcRequestBodyProcessFilter);
        registration.setName("svcRequestBodyProcessFilter");
        registration.addUrlPatterns(svcRegisterComponent.getSvcPath(svcDefinition -> true));
        registration.setOrder(1);
        return registration;
    }

    /**
     * Create and return {@link FilterRegistrationBean} for {@link SvcIPSecureFilter}
     */
    @Bean
    public FilterRegistrationBean<SvcIPSecureFilter> svcIPSecureFilterFilterRegistrationBean(SvcRegisterComponent svcRegisterComponent, SvcIPSecureFilter svcIpSecureFilter) {
        FilterRegistrationBean<SvcIPSecureFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(svcIpSecureFilter);
        registration.setName("svcIpSecureFilter");
        String[] svcPath = svcRegisterComponent.getSvcPath(svcDefinition -> svcDefinition.getFeature().getIpSecure().length > 0);
        if (svcPath.length == 0) {
            registration.setEnabled(false);
        } else {
            registration.addUrlPatterns(svcPath);
        }
        registration.setOrder(5);
        return registration;
    }

    /**
     * Create and return {@link FilterRegistrationBean} for {@link SvcRequestVerifyFilter}
     */
    @Bean
    public FilterRegistrationBean<SvcRequestVerifyFilter> svcRequestVerifyFilterFilterRegistrationBean(SvcRegisterComponent svcRegisterComponent, SvcRequestVerifyFilter svcRequestVerifyFilter) {
        FilterRegistrationBean<SvcRequestVerifyFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(svcRequestVerifyFilter);
        registration.setName("svcReqModelValidFilter");
        String[] svcPath = svcRegisterComponent.getSvcPath(svcDefinition -> true);
        if (svcPath.length == 0) {
            registration.setEnabled(false);
        } else {
            registration.addUrlPatterns(svcPath);
        }
        registration.setOrder(10);
        return registration;
    }

    /**
     * Create and return {@link FilterRegistrationBean} for {@link SvcLogFilter}
     */
    @Bean
    public FilterRegistrationBean<SvcLogFilter> svcLogFilterFilterRegistrationBean(SvcRegisterComponent svcRegisterComponent, SvcLogFilter svcLogFilter) {
        FilterRegistrationBean<SvcLogFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(svcLogFilter);
        registration.setName("svcLogFilter");
        String[] svcPath = svcRegisterComponent.getSvcPath(svcDefinition -> svcDefinition.getFeature().isLogging());
        if (svcPath.length == 0) {
            registration.setEnabled(false);
        } else {
            registration.addUrlPatterns(svcPath);
        }
        registration.setOrder(15);
        return registration;
    }

    /**
     * Create and return SvcPreProcessFilter instance
     */
    @Bean
    public SvcProcessFilter svcPreProcessFilter(SvcInfo svcInfo, SvcMsgHandler svcMsgHandler, SvcProperties svcProperties, SvcLoggingHandler svcLoggingHandler, ExceptionHandlerResolver exceptionHandlerResolver, SvcRegisterComponent svcRegisterComponent, JacksonUtils jacksonUtils, SvcResUtils svcResUtils) {
        return new SvcProcessFilter(svcInfo, svcMsgHandler, svcProperties, svcLoggingHandler, exceptionHandlerResolver, svcRegisterComponent, jacksonUtils, svcResUtils);
    }

    /**
     * Create and return SvcRequestBodyProcessFilter instance
     */
    @Bean
    public SvcRequestBodyProcessFilter svcRequestBodyProcessFilter(SvcInfo svcInfo, SvcRequestBodyProcessHandler svcRequestBodyProcessHandler, JacksonUtils jacksonUtils) {
        return new SvcRequestBodyProcessFilter(svcInfo, svcRequestBodyProcessHandler, jacksonUtils);
    }

    /**
     * Create and return SvcIPSecureFilter instance
     */
    @Bean
    public SvcIPSecureFilter svcIPSecureFilter(SvcInfo svcInfo) {
        return new SvcIPSecureFilter(svcInfo);
    }

    /**
     * Create and return SvcLogFilter instance
     */
    @Bean
    public SvcLogFilter svcLogFilter(SvcInfo svcInfo, SvcLoggingHandler svcLoggingHandler, SerialNumberGeneratorHandler serialNumberGeneratorHandler) {
        return new SvcLogFilter(svcInfo, svcLoggingHandler, serialNumberGeneratorHandler);
    }

    /**
     * Create and return SvcRequestVerifyFilter instance
     */
    @Bean
    public SvcRequestVerifyFilter svcReqModelValidFilter(SvcInfo svcInfo, Validator validator, JacksonUtils jacksonUtils) {
        return new SvcRequestVerifyFilter(svcInfo, validator, jacksonUtils);
    }

}
