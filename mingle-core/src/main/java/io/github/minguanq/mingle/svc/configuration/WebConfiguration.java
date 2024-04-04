package io.github.minguanq.mingle.svc.configuration;

import io.github.minguanq.mingle.svc.SvcRequestArgumentResolver;
import io.github.minguanq.mingle.svc.SvcResponseResolver;
import io.github.minguanq.mingle.svc.filter.SvcInfo;
import io.github.minguanq.mingle.svc.utils.JacksonUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration for spring web
 *
 * @author Ming
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    private final SvcInfo svcInfo;
    private final JacksonUtils jacksonUtils;

    public WebConfiguration(SvcInfo svcInfo, JacksonUtils jacksonUtils) {
        this.svcInfo = svcInfo;
        this.jacksonUtils = jacksonUtils;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(svcReqArgumentResolver());
    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
        handlers.add(svcReturnValueResolver());
    }

    @Bean
    public SvcRequestArgumentResolver svcReqArgumentResolver() {
        return new SvcRequestArgumentResolver(svcInfo);
    }

    @Bean
    public SvcResponseResolver svcReturnValueResolver() {
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(new MappingJackson2HttpMessageConverter(jacksonUtils.getObjectMapper()));
        return new SvcResponseResolver(converters);
    }


}
