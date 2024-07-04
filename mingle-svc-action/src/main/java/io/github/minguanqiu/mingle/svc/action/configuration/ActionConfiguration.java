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
 * Configuration for all action bean.
 *
 * @author Qiu Guan Ming
 */
@Configuration
public class ActionConfiguration {

  /**
   * Create new a ActionProperties instance.
   *
   * @return return a ActionProperties instance.
   */
  @Bean
  @Primary
  @ConfigurationProperties("mingle.svc.action")
  public ActionProperties actionProperties() {
    return new ActionProperties();
  }

  /**
   * Create new a JacksonUtils instance.
   *
   * @return return a JacksonUtils instance.
   */
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

  /**
   * Create new a ActionLoggingHandler instance.
   *
   * @return return a ActionLoggingHandler instance.
   */
  @Bean
  @ConditionalOnMissingBean
  public ActionLoggingHandler actionLoggingHandler() {
    return new ActionLoggingDefaultHandlerImpl(actionLogJacksonUtils());
  }

  /**
   * Create new a ActionExceptionHandlerResolver instance.
   *
   * @param abstractExceptionHandlers the list of action exception handlers.
   * @return return a ActionExceptionHandlerResolver instance.
   */
  @Bean
  public ActionExceptionHandlerResolver actionExceptionHandlerResolver(
      List<AbstractActionExceptionHandler<?>> abstractExceptionHandlers) {
    return new ActionExceptionHandlerResolver(abstractExceptionHandlers);
  }

  /**
   * Create new a ActionAutoBreakExceptionHandler instance.
   *
   * @param svcInfo            the service information.
   * @param codeMessageHandler the code message handler.
   * @return return a ActionAutoBreakExceptionHandler instance.
   */
  @Bean
  public ActionAutoBreakExceptionHandler actionAutoBreakExceptionHandler(SvcInfo svcInfo,
      CodeMessageHandler codeMessageHandler) {
    return new ActionAutoBreakExceptionHandler(svcInfo, codeMessageHandler);
  }

  /**
   * Create new a AllActionExceptionHandler instance.
   *
   * @return return a AllActionExceptionHandler instance.
   */
  @Bean
  public AllActionExceptionHandler allActionExceptionHandler() {
    return new AllActionExceptionHandler();
  }

}
