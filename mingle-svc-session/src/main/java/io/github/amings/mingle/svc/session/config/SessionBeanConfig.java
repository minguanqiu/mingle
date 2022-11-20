package io.github.amings.mingle.svc.session.config;

import io.github.amings.mingle.svc.session.handler.JwtKeyHandler;
import io.github.amings.mingle.svc.session.handler.impl.JwtKeyHandlerDefaultImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Ming
 */
@Configuration
public class SessionBeanConfig {

    @Bean
    @ConditionalOnMissingBean
    public JwtKeyHandler jwtKeyHandler() {
        return new JwtKeyHandlerDefaultImpl();
    }

}
