package io.github.minguanqiu.mingle.svc.action.rest.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.github.minguanqiu.mingle.svc.action.rest.configuration.properties.RestActionProperties;
import io.github.minguanqiu.mingle.svc.action.rest.handler.RestClientHandler;
import io.github.minguanqiu.mingle.svc.action.rest.handler.impl.RestClientDefaultHandler;
import io.github.minguanqiu.mingle.svc.action.rest.json.view.Views;
import io.github.minguanqiu.mingle.svc.utils.JacksonUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for action rest bean
 *
 * @author Qiu Guan Ming
 */

@Configuration
public class ActionRestConfiguration {

  @Bean
  @ConfigurationProperties("mingle.svc.action")
  public RestActionProperties restActionProperties() {
    return new RestActionProperties();
  }

  @Bean
  @ConditionalOnMissingBean
  public RestClientHandler restClientHandler() {
    return new RestClientDefaultHandler();
  }

  @Bean
  @ConditionalOnMissingBean(name = "restActionJacksonUtils")
  public JacksonUtils restActionJacksonUtils() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    objectMapper.setConfig(objectMapper.getSerializationConfig().withView(Views.class));
    return new JacksonUtils(objectMapper);
  }

}
