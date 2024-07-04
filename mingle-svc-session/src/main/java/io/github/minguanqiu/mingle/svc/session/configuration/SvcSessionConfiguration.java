package io.github.minguanqiu.mingle.svc.session.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.minguanqiu.mingle.svc.session.configuration.properties.SvcSessionProperties;
import io.github.minguanqiu.mingle.svc.session.dao.SvcSessionDao;
import io.github.minguanqiu.mingle.svc.session.handler.SessionTokenHandler;
import io.github.minguanqiu.mingle.svc.session.handler.SvcSessionFeatureHandler;
import io.github.minguanqiu.mingle.svc.session.handler.TokenKeyHandler;
import io.github.minguanqiu.mingle.svc.session.handler.impl.SessionTokenHandlerDefaultImpl;
import io.github.minguanqiu.mingle.svc.session.handler.impl.SvcSessionFeatureDefaultImpl;
import io.github.minguanqiu.mingle.svc.session.handler.impl.TokenKeyHandlerImpl;
import io.github.minguanqiu.mingle.svc.session.register.SvcSessionRegisterImpl;
import io.github.minguanqiu.mingle.svc.session.utils.SessionUtils;
import io.github.minguanqiu.mingle.svc.utils.JacksonUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for session bean.
 *
 * @author Qiu Guan Ming
 */
@Configuration
public class SvcSessionConfiguration {

  @Bean
  @ConfigurationProperties("mingle.svc.session")
  public SvcSessionProperties sessionProperties() {
    return new SvcSessionProperties();
  }

  @Bean
  public SvcSessionRegisterImpl svcSessionRegister(SvcSessionFeatureHandler sessionFeatureHandler) {
    return new SvcSessionRegisterImpl(sessionFeatureHandler);
  }

  @Bean
  public SessionTokenHandler sessionTokenHandler() {
    return new SessionTokenHandlerDefaultImpl(tokenKeyHandler());
  }

  @Bean
  @ConditionalOnMissingBean
  public SvcSessionFeatureHandler sessionFeatureHandler() {
    return new SvcSessionFeatureDefaultImpl();
  }

  @Bean
  @ConditionalOnMissingBean
  public TokenKeyHandler tokenKeyHandler() {
    return new TokenKeyHandlerImpl();
  }

  @Bean
  public SessionUtils sessionUtils(SvcSessionDao svcSessionDao) {
    return new SessionUtils(svcSessionDao, sessionTokenHandler(), sessionJacksonUtils());
  }

  @Bean
  public JacksonUtils sessionJacksonUtils() {
    return new JacksonUtils(new ObjectMapper());
  }

}
