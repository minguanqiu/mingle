package io.github.minguanqiu.mingle.svc.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.github.minguanqiu.mingle.svc.Service;
import io.github.minguanqiu.mingle.svc.configuration.properties.SvcProperties;
import io.github.minguanqiu.mingle.svc.exception.handler.AbstractExceptionHandler;
import io.github.minguanqiu.mingle.svc.exception.handler.AllExceptionHandler;
import io.github.minguanqiu.mingle.svc.exception.handler.BreakFilterProcessExceptionHandler;
import io.github.minguanqiu.mingle.svc.exception.handler.BreakSvcProcessExceptionHandler;
import io.github.minguanqiu.mingle.svc.exception.handler.resolver.ExceptionHandlerResolver;
import io.github.minguanqiu.mingle.svc.filter.SvcInfo;
import io.github.minguanqiu.mingle.svc.handler.CodeMessageHandler;
import io.github.minguanqiu.mingle.svc.handler.CodeMessageListHandler;
import io.github.minguanqiu.mingle.svc.handler.SerialNumberGeneratorHandler;
import io.github.minguanqiu.mingle.svc.handler.SvcFeatureHandler;
import io.github.minguanqiu.mingle.svc.handler.SvcLoggingHandler;
import io.github.minguanqiu.mingle.svc.handler.SvcPathHandler;
import io.github.minguanqiu.mingle.svc.handler.SvcRequestBodyProcessHandler;
import io.github.minguanqiu.mingle.svc.handler.SvcResponseHandler;
import io.github.minguanqiu.mingle.svc.handler.impl.CodeMessageHandlerDefaultImpl;
import io.github.minguanqiu.mingle.svc.handler.impl.SerialNumberGeneratorHandlerDefaultImpl;
import io.github.minguanqiu.mingle.svc.handler.impl.SvcFeatureHandlerDefaultImpl;
import io.github.minguanqiu.mingle.svc.handler.impl.SvcLoggingHandlerDefaultImpl;
import io.github.minguanqiu.mingle.svc.handler.impl.SvcPathHandlerDefaultImpl;
import io.github.minguanqiu.mingle.svc.handler.impl.SvcResponseDefaultImpl;
import io.github.minguanqiu.mingle.svc.handler.impl.SvcSvcRequestBodyProcessHandlerDefaultImpl;
import io.github.minguanqiu.mingle.svc.handler.model.SvcFeature;
import io.github.minguanqiu.mingle.svc.json.view.Views;
import io.github.minguanqiu.mingle.svc.register.SvcFeatureRegister;
import io.github.minguanqiu.mingle.svc.register.SvcFeatureRegisterImpl;
import io.github.minguanqiu.mingle.svc.register.SvcRegister;
import io.github.minguanqiu.mingle.svc.utils.JacksonUtils;
import io.github.minguanqiu.mingle.svc.utils.SvcResUtils;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * Configuration for all service bean.
 *
 * @author Qiu Guan Ming
 */
@Configuration
public class SvcConfiguration {

  /**
   * Create a new SvcProperties instance.
   *
   * @return return the service properties.
   */
  @Bean
  @ConfigurationProperties("mingle.svc.properties")
  public SvcProperties svcPropertySource() {
    return new SvcProperties();
  }

  /**
   * Create a new SvcRegister instance.
   *
   * @param svcProperties                the service properties.
   * @param services                     the list of services.
   * @param requestMappingHandlerMapping the request mapping handler.
   * @param svcPathHandler               the service path handler.
   * @param svcFeatureRegister           the service feature register.
   * @return return the service register.
   */
  @Bean
  public SvcRegister svcRegister(SvcProperties svcProperties, List<Service<?, ?>> services,
      RequestMappingHandlerMapping requestMappingHandlerMapping, SvcPathHandler svcPathHandler,
      List<SvcFeatureRegister<?>> svcFeatureRegister) {
    return new SvcRegister(svcProperties, services, requestMappingHandlerMapping, svcPathHandler,
        svcFeatureRegister);
  }

  /**
   * Create a new SvcFeatureRegister instance.
   *
   * @param svcProperties     the service properties.
   * @param svcFeatureHandler service feature handler.
   * @return return the service feature register.
   */
  @Bean
  public SvcFeatureRegister<SvcFeature> svcFeatureRegister(SvcProperties svcProperties,
      SvcFeatureHandler svcFeatureHandler) {
    return new SvcFeatureRegisterImpl(svcProperties, svcFeatureHandler);
  }

  /**
   * Create a new SvcLoggingHandler instance,implements {@link SvcLoggingHandler} to override
   * default impl.
   *
   * @return return the service logging handler.
   */
  @Bean
  @ConditionalOnMissingBean
  public SvcLoggingHandler svcLogHandler() {
    return new SvcLoggingHandlerDefaultImpl(svcLogJacksonUtils());
  }

  /**
   * Create a new SvcRequestBodyProcessHandler instance,implements
   * {@link SvcRequestBodyProcessHandler} to override default impl.
   *
   * @return return the service request body process handler.
   */
  @Bean
  @ConditionalOnMissingBean
  public SvcRequestBodyProcessHandler svcRequestBodyProcessHandler() {
    return new SvcSvcRequestBodyProcessHandlerDefaultImpl();
  }

  /**
   * Create a new SvcFeatureHandler instance,implements {@link SvcFeatureHandler} to override
   * default impl.
   *
   * @return return the service feature handler.
   */
  @Bean
  @ConditionalOnMissingBean
  public SvcFeatureHandler svcFeatureHandler() {
    return new SvcFeatureHandlerDefaultImpl();
  }

  /**
   * Create a new CodeMessageHandler instance.
   *
   * @param codeMessageListHandlers the code message list handler.
   * @return return the code message handler.
   */
  @Bean
  public CodeMessageHandler svcMsgHandler(List<CodeMessageListHandler> codeMessageListHandlers) {
    return new CodeMessageHandlerDefaultImpl(codeMessageListHandlers);
  }

  /**
   * Create a new SvcResponseHandler instance.
   *
   * @return return the service response handler.
   */
  @Bean
  @ConditionalOnMissingBean
  public SvcResponseHandler svcResModelHandler() {
    return new SvcResponseDefaultImpl();
  }

  /**
   * Create a new SvcPathHandler instance.
   *
   * @return return the service path handler.
   */
  @Bean
  @ConditionalOnMissingBean
  public SvcPathHandler svcPathHandler() {
    return new SvcPathHandlerDefaultImpl();
  }

  /**
   * Create a new JacksonUtils instance for default.
   *
   * @return return the jackson utils.
   */
  @Bean
  @Primary
  @ConditionalOnMissingBean(name = "svcJacksonUtils")
  public JacksonUtils svcJacksonUtils() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    return new JacksonUtils(objectMapper);
  }

  /**
   * Create a new JacksonUtils instance for logging.
   *
   * @return return the jackson utils.
   */
  @Bean
  @ConditionalOnMissingBean(name = "svcLogJacksonUtils")
  public JacksonUtils svcLogJacksonUtils() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    objectMapper.setConfig(objectMapper.getSerializationConfig().withView(Views.class));
    objectMapper.findAndRegisterModules();
    return new JacksonUtils(objectMapper);
  }

  /**
   * Create a new SerialNumberGeneratorHandler instance.
   *
   * @return return the service serial number generator handler.
   */
  @Bean
  @ConditionalOnMissingBean
  public SerialNumberGeneratorHandler svcSerialNumberGenerator() {
    return new SerialNumberGeneratorHandlerDefaultImpl();
  }

  /**
   * Create a new ExceptionHandlerResolver instance.
   *
   * @param abstractExceptionHandlers the list of exception handlers.
   * @return return the service exception resolver.
   */
  @Bean
  public ExceptionHandlerResolver exceptionHandlerResolver(
      List<AbstractExceptionHandler<?>> abstractExceptionHandlers) {
    return new ExceptionHandlerResolver(abstractExceptionHandlers);
  }

  /**
   * Create a new SvcResUtils instance.
   *
   * @param svcResponseHandler the service response handler.
   * @param jacksonUtils       the jackson utils.
   * @return return the service response utils.
   */
  @Bean
  public SvcResUtils svcResUtils(SvcResponseHandler svcResponseHandler, JacksonUtils jacksonUtils) {
    return new SvcResUtils(svcResponseHandler, jacksonUtils);
  }

  /**
   * Create a new AllExceptionHandler instance.
   *
   * @param svcInfo the service information.
   * @return return the all exception handler.
   */
  @Bean
  public AllExceptionHandler allExceptionHandler(SvcInfo svcInfo) {
    return new AllExceptionHandler(svcInfo);
  }

  /**
   * Create a new BreakSvcProcessExceptionHandler instance.
   *
   * @param svcInfo the service information.
   * @return return the break service process logic exception handler.
   */
  @Bean
  public BreakSvcProcessExceptionHandler breakSvcProcessExceptionHandler(SvcInfo svcInfo) {
    return new BreakSvcProcessExceptionHandler(svcInfo);
  }

  /**
   * Create a new BreakFilterProcessExceptionHandler instance.
   *
   * @param svcInfo the service information.
   * @return return the break service filter process logic exception handler.
   */
  @Bean
  public BreakFilterProcessExceptionHandler breakFilterProcessExceptionHandler(SvcInfo svcInfo) {
    return new BreakFilterProcessExceptionHandler(svcInfo);
  }

}
