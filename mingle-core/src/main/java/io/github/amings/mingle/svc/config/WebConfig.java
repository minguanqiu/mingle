package io.github.amings.mingle.svc.config;

import io.github.amings.mingle.svc.SvcReqArgumentResolver;
import io.github.amings.mingle.svc.SvcResArgumentResolver;
import io.github.amings.mingle.svc.SvcReturnValueResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
 * Svc Web configuration
 *
 * @author Ming
 */

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${mingle.svc.path:/svc}")
    private String svcPath;
    @Autowired
    MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Bean
    SvcReqArgumentResolver svcReqArgumentResolver() {
        return new SvcReqArgumentResolver();
    }

    @Bean
    SvcResArgumentResolver svcResArgumentResolver() {
        return new SvcResArgumentResolver();
    }

    public String getSvcPath() {
        return svcPath;
    }

    @Bean
    SvcReturnValueResolver svcReturnValueResolver() {
        List<HttpMessageConverter<?>> objects = new ArrayList<>();
        objects.add(mappingJackson2HttpMessageConverter);
        return new SvcReturnValueResolver(objects);
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

}
