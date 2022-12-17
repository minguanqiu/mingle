package io.github.amings.mingle.svc.config;

import io.github.amings.mingle.svc.SvcReqArgumentResolver;
import io.github.amings.mingle.svc.SvcResArgumentResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(svcReqArgumentResolver());
        resolvers.add(svcResArgumentResolver());
    }

}
