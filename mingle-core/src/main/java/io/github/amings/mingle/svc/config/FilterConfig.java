package io.github.amings.mingle.svc.config;

import io.github.amings.mingle.svc.component.SvcBinderComponent;
import io.github.amings.mingle.svc.filter.SvcLogFilter;
import io.github.amings.mingle.svc.filter.SvcPreProcessFilter;
import io.github.amings.mingle.svc.filter.SvcReqModelVerifyFilter;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    SvcBinderComponent svcBinderComponent;

    /**
     * Svc preProcess filter bean
     * @return FilterRegistrationBean
     * @see SvcPreProcessFilter
     */
    @Bean
    public FilterRegistrationBean<SvcPreProcessFilter> svcFilterRegistration() {
        FilterRegistrationBean<SvcPreProcessFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(svcPreProcessFilter());
        registration.setName("svcPreProcessFilter");
        registration.addUrlPatterns(svcBinderComponent.getSvcPathList().toArray(new String[0]));
        registration.setOrder(5);
        return registration;
    }

    /**
     * Svc request model javax bean valid filter bean
     * @return FilterRegistrationBean
     * @see SvcReqModelVerifyFilter
     */
    @Bean
    public FilterRegistrationBean<SvcReqModelVerifyFilter> beanValidFilterRegistration() {
        FilterRegistrationBean<SvcReqModelVerifyFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(svcReqModelValidFilter());
        registration.setName("svcReqModelValidFilter");
        registration.addUrlPatterns(svcBinderComponent.getSvcValidBeanPathList().toArray(new String[0]));
        registration.setOrder(10);
        return registration;
    }

    /**
     * logging filter bean
     * @return FilterRegistrationBean
     * @see SvcLogFilter
     */
    @Bean
    public FilterRegistrationBean<SvcLogFilter> svcLogFilterRegistration() {
        FilterRegistrationBean<SvcLogFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(svcLogFilter());
        registration.setName("svcLogFilter");
        registration.addUrlPatterns(svcBinderComponent.getSvcLogPathList().toArray(new String[0]));
        registration.setOrder(11);
        return registration;
    }

    @Bean
    public SvcPreProcessFilter svcPreProcessFilter() {
        return new SvcPreProcessFilter();
    }

    @Bean
    public SvcLogFilter svcLogFilter() {
        return new SvcLogFilter();
    }

    @Bean
    public SvcReqModelVerifyFilter svcReqModelValidFilter() {
        return new SvcReqModelVerifyFilter();
    }

}
