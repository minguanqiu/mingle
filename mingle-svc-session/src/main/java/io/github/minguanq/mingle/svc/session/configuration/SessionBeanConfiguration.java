package io.github.minguanq.mingle.svc.session.configuration;

import io.github.minguanq.mingle.svc.session.dao.SessionDao;
import io.github.minguanq.mingle.svc.session.handler.SessionTokenHandler;
import io.github.minguanq.mingle.svc.session.handler.TokenKeyHandler;
import io.github.minguanq.mingle.svc.session.handler.impl.SessionTokenHandlerDefaultImpl;
import io.github.minguanq.mingle.svc.session.handler.impl.TokenKeyHandlerImpl;
import io.github.minguanq.mingle.svc.session.utils.SessionUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for session bean
 *
 * @author Ming
 */
@Configuration
public class SessionBeanConfiguration {

    @Bean
    public SessionTokenHandler sessionTokenHandler() {
        return new SessionTokenHandlerDefaultImpl(tokenKeyHandler());
    }


    @Bean
    @ConditionalOnMissingBean
    public TokenKeyHandler tokenKeyHandler() {
        return new TokenKeyHandlerImpl();
    }

    @Bean
    public SessionUtils sessionUtils(SessionDao sessionDao) {
        return new SessionUtils(sessionDao, sessionTokenHandler());
    }

}
