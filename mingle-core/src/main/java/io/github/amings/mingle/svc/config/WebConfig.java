package io.github.amings.mingle.svc.config;

import io.github.amings.mingle.svc.SvcReqArgumentResolver;
import io.github.amings.mingle.svc.SvcResArgumentResolver;
import io.github.amings.mingle.svc.SvcReturnValueResolver;
import io.github.amings.mingle.svc.json.converter.JacksonMessageConverter;
import io.github.amings.mingle.utils.JacksonUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * Svc Web configuration
 *
 * @author Ming
 */

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JacksonUtils jacksonUtils;

    public WebConfig(JacksonUtils jacksonUtils) {
        this.jacksonUtils = jacksonUtils;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(svcReqArgumentResolver());
        resolvers.add(svcResArgumentResolver());
    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
        handlers.add(svcReturnValueResolver());
    }

    @Bean
    public SvcReqArgumentResolver svcReqArgumentResolver() {
        return new SvcReqArgumentResolver();
    }

    @Bean
    public SvcResArgumentResolver svcResArgumentResolver() {
        return new SvcResArgumentResolver();
    }

    @Bean
    public SvcReturnValueResolver svcReturnValueResolver() {
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(jacksonMessageConverter());
        return new SvcReturnValueResolver(converters);
    }

    @Bean
    public JacksonMessageConverter jacksonMessageConverter() {
        return new JacksonMessageConverter(jacksonUtils.getObjectMapper());
    }

}
