package io.github.amings.mingle.svc.configuration;

import io.github.amings.mingle.svc.component.SvcBinderComponent;
import io.github.amings.mingle.svc.configuration.properties.SvcProperties;
import io.github.amings.mingle.svc.exception.handler.resolver.ExceptionHandlerResolver;
import io.github.amings.mingle.svc.filter.*;
import io.github.amings.mingle.svc.handler.SerialNumberGeneratorHandler;
import io.github.amings.mingle.svc.handler.SvcLogHandler;
import io.github.amings.mingle.svc.handler.SvcMsgHandler;
import io.github.amings.mingle.svc.handler.SvcRequestBodyProcessHandler;
import io.github.amings.mingle.svc.utils.JacksonUtils;
import io.github.amings.mingle.svc.utils.SvcResUtils;
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
    public FilterRegistrationBean<SvcProcessFilter> svcPreProcessFilterFilterRegistrationBean(SvcBinderComponent svcBinderComponent, SvcProcessFilter svcProcessFilter) {
        FilterRegistrationBean<SvcProcessFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(svcProcessFilter);
        registration.setName("svcPreProcessFilter");
        registration.addUrlPatterns(svcBinderComponent.findSvcPath(svcBinderModel -> true));
        registration.setOrder(0);
        return registration;
    }

    /**
     * Create and return {@link FilterRegistrationBean} for {@link SvcRequestBodyProcessFilter}
     */
    @Bean
    public FilterRegistrationBean<SvcRequestBodyProcessFilter> svcRequestBodyProcessFilterFilterRegistrationBean(SvcBinderComponent svcBinderComponent, SvcRequestBodyProcessFilter svcRequestBodyProcessFilter) {
        FilterRegistrationBean<SvcRequestBodyProcessFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(svcRequestBodyProcessFilter);
        registration.setName("svcRequestBodyProcessFilter");
        registration.addUrlPatterns(svcBinderComponent.findSvcPath(svcBinderModel -> true));
        registration.setOrder(1);
        return registration;
    }

    /**
     * Create and return {@link FilterRegistrationBean} for {@link SvcIPSecureFilter}
     */
    @Bean
    public FilterRegistrationBean<SvcIPSecureFilter> svcIPSecureFilterFilterRegistrationBean(SvcBinderComponent svcBinderComponent, SvcIPSecureFilter svcIpSecureFilter) {
        FilterRegistrationBean<SvcIPSecureFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(svcIpSecureFilter);
        registration.setName("svcIpSecureFilter");
        String[] svcPath = svcBinderComponent.findSvcPath(svcBinderModel -> svcBinderModel.getSvc().ipSecure());
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
    public FilterRegistrationBean<SvcRequestVerifyFilter> svcRequestVerifyFilterFilterRegistrationBean(SvcBinderComponent svcBinderComponent, SvcRequestVerifyFilter svcRequestVerifyFilter) {
        FilterRegistrationBean<SvcRequestVerifyFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(svcRequestVerifyFilter);
        registration.setName("svcReqModelValidFilter");
        String[] svcPath = svcBinderComponent.findSvcPath(svcBinderModel -> true);
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
    public FilterRegistrationBean<SvcLogFilter> svcLogFilterFilterRegistrationBean(SvcBinderComponent svcBinderComponent, SvcLogFilter svcLogFilter) {
        FilterRegistrationBean<SvcLogFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(svcLogFilter);
        registration.setName("svcLogFilter");
        String[] svcPath = svcBinderComponent.findSvcPath(svcBinderModel -> svcBinderModel.getSvc().logging());
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
    public SvcProcessFilter svcPreProcessFilter(SvcInfo svcInfo, SvcMsgHandler svcMsgHandler, SvcProperties svcProperties, SvcLogHandler svcLogHandler, ExceptionHandlerResolver exceptionHandlerResolver, SvcBinderComponent svcBinderComponent, JacksonUtils jacksonUtils, SvcResUtils svcResUtils) {
        return new SvcProcessFilter(svcInfo, svcMsgHandler, svcProperties, svcLogHandler, exceptionHandlerResolver, svcBinderComponent, jacksonUtils, svcResUtils);
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
    public SvcLogFilter svcLogFilter(SvcInfo svcInfo, SvcLogHandler svcLogHandler, SerialNumberGeneratorHandler serialNumberGeneratorHandler) {
        return new SvcLogFilter(svcInfo, svcLogHandler, serialNumberGeneratorHandler);
    }

    /**
     * Create and return SvcRequestVerifyFilter instance
     */
    @Bean
    public SvcRequestVerifyFilter svcReqModelValidFilter(SvcInfo svcInfo, Validator validator, JacksonUtils jacksonUtils) {
        return new SvcRequestVerifyFilter(svcInfo, validator, jacksonUtils);
    }

}
