package io.github.amings.mingle.svc.config;

import io.github.amings.mingle.svc.component.SvcBinderComponent;
import io.github.amings.mingle.svc.config.properties.SvcProperties;
import io.github.amings.mingle.svc.exception.handler.resolver.ExceptionHandlerResolver;
import io.github.amings.mingle.svc.filter.SvcInfo;
import io.github.amings.mingle.svc.filter.SvcLogFilter;
import io.github.amings.mingle.svc.filter.SvcPreProcessFilter;
import io.github.amings.mingle.svc.filter.SvcReqModelVerifyFilter;
import io.github.amings.mingle.svc.handler.IPHandler;
import io.github.amings.mingle.svc.handler.PayLoadDecryptionHandler;
import io.github.amings.mingle.svc.handler.SvcLogHandler;
import io.github.amings.mingle.svc.handler.SvcMsgHandler;
import io.github.amings.mingle.utils.JacksonUtils;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Svc filter configuration
 *
 * @author Ming
 */

@Configuration
public class FilterConfig {

    private final SvcBinderComponent svcBinderComponent;

    public FilterConfig(SvcBinderComponent svcBinderComponent) {
        this.svcBinderComponent = svcBinderComponent;
    }

    /**
     * Svc preProcess filter bean
     *
     * @return FilterRegistrationBean
     * @see SvcPreProcessFilter
     */
    @Bean
    public FilterRegistrationBean<SvcPreProcessFilter> svcFilterRegistration(SvcPreProcessFilter svcPreProcessFilter) {
        FilterRegistrationBean<SvcPreProcessFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(svcPreProcessFilter);
        registration.setName("svcPreProcessFilter");
        registration.addUrlPatterns(svcBinderComponent.findSvcPath(svcBinderModel -> true));
        registration.setOrder(0);
        return registration;
    }

    /**
     * Svc request model javax bean valid filter bean
     *
     * @return FilterRegistrationBean
     * @see SvcReqModelVerifyFilter
     */
    @Bean
    public FilterRegistrationBean<SvcReqModelVerifyFilter> beanValidFilterRegistration(SvcReqModelVerifyFilter svcReqModelVerifyFilter) {
        FilterRegistrationBean<SvcReqModelVerifyFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(svcReqModelVerifyFilter);
        registration.setName("svcReqModelValidFilter");
        String[] svcPath = svcBinderComponent.findSvcPath(svcBinderModel -> !svcBinderModel.isReqCustom());
        if (svcPath.length == 0) {
            registration.setEnabled(false);
        } else {
            registration.addUrlPatterns(svcPath);
        }
        registration.setOrder(10);
        return registration;
    }

    /**
     * logging filter bean
     *
     * @return FilterRegistrationBean
     * @see SvcLogFilter
     */
    @Bean
    public FilterRegistrationBean<SvcLogFilter> svcLogFilterRegistration(SvcLogFilter svcLogFilter) {
        FilterRegistrationBean<SvcLogFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(svcLogFilter);
        registration.setName("svcLogFilter");
        String[] svcPath = svcBinderComponent.findSvcPath(svcBinderModel -> svcBinderModel.getSvc().log());
        if (svcPath.length == 0) {
            registration.setEnabled(false);
        } else {
            registration.addUrlPatterns(svcPath);
        }
        registration.setOrder(15);
        return registration;
    }

    @Bean
    public SvcPreProcessFilter svcPreProcessFilter(SvcInfo svcInfo, SvcMsgHandler svcMsgHandler, SvcProperties svcProperties, PayLoadDecryptionHandler payLoadDecryptionHandler, SvcLogHandler svcLogHandler, ExceptionHandlerResolver exceptionHandlerResolver, IPHandler ipHandler, SvcBinderComponent svcBinderComponent, JacksonUtils jacksonUtils) {
        return new SvcPreProcessFilter(svcInfo, svcMsgHandler, svcProperties, payLoadDecryptionHandler, svcLogHandler, exceptionHandlerResolver, ipHandler, svcBinderComponent, jacksonUtils);
    }

    @Bean
    public SvcLogFilter svcLogFilter(SvcInfo svcInfo, SvcMsgHandler svcMsgHandler, SvcProperties svcProperties, SvcLogHandler svcLogHandler, @Qualifier("svcLogJacksonUtils") JacksonUtils jacksonUtils) {
        return new SvcLogFilter(svcInfo, svcMsgHandler, svcProperties, svcLogHandler, jacksonUtils);
    }

    @Bean
    public SvcReqModelVerifyFilter svcReqModelValidFilter(SvcInfo svcInfo, SvcMsgHandler svcMsgHandler, SvcProperties svcProperties, Validator validator, JacksonUtils jacksonUtils) {
        return new SvcReqModelVerifyFilter(svcInfo, svcMsgHandler, svcProperties, validator, jacksonUtils);
    }

}
