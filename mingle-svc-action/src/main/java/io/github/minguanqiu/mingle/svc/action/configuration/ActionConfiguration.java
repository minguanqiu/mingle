package io.github.minguanqiu.mingle.svc.action.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.github.minguanqiu.mingle.svc.action.configuration.properties.ActionProperties;
import io.github.minguanqiu.mingle.svc.action.exception.handler.ActionAutoBreakExceptionHandler;
import io.github.minguanqiu.mingle.svc.action.exception.handler.AllActionExceptionHandler;
import io.github.minguanqiu.mingle.svc.action.exception.handler.abs.AbstractActionExceptionHandler;
import io.github.minguanqiu.mingle.svc.action.exception.resolver.ActionExceptionHandlerResolver;
import io.github.minguanqiu.mingle.svc.action.handler.ActionLoggingHandler;
import io.github.minguanqiu.mingle.svc.action.handler.impl.ActionLoggingDefaultHandlerImpl;
import io.github.minguanqiu.mingle.svc.filter.SvcInfo;
import io.github.minguanqiu.mingle.svc.handler.CodeMessageHandler;
import io.github.minguanqiu.mingle.svc.json.view.Views;
import io.github.minguanqiu.mingle.svc.utils.JacksonUtils;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Configuration for action
 *
 * @author Qiu Guan Ming
 */
@Configuration
public class ActionConfiguration {

  @Bean
  @Primary
  @ConfigurationProperties("mingle.svc.action")
  public ActionProperties actionProperties() {
    return new ActionProperties();
  }

  @Bean("actionLogJacksonUtils")
  @ConditionalOnMissingBean(name = "actionLogJacksonUtils")
  public JacksonUtils actionLogJacksonUtils() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    objectMapper.setConfig(objectMapper.getSerializationConfig().withView(Views.class));
    objectMapper.findAndRegisterModules();
    return new JacksonUtils(objectMapper);
  }

  @Bean
  @ConditionalOnMissingBean
  public ActionLoggingHandler actionLoggingHandler() {
    return new ActionLoggingDefaultHandlerImpl(actionLogJacksonUtils());
  }

  @Bean
  public ActionExceptionHandlerResolver actionExceptionHandlerResolver(
      List<AbstractActionExceptionHandler<?>> abstractExceptionHandlers) {
    return new ActionExceptionHandlerResolver(abstractExceptionHandlers);
  }

  @Bean
  public ActionAutoBreakExceptionHandler actionAutoBreakExceptionHandler(SvcInfo svcInfo,
      CodeMessageHandler codeMessageHandler) {
    return new ActionAutoBreakExceptionHandler(svcInfo, codeMessageHandler);
  }

  @Bean
  public AllActionExceptionHandler allActionExceptionHandler() {
    return new AllActionExceptionHandler();
  }

}
