package io.github.minguanqiu.mingle.svc.data.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.github.minguanqiu.mingle.svc.data.aspect.DaoLogAspect;
import io.github.minguanqiu.mingle.svc.data.configuration.properties.SvcDataProperties;
import io.github.minguanqiu.mingle.svc.data.handler.DaoLoggingHandler;
import io.github.minguanqiu.mingle.svc.data.handler.impl.DaoLoggingHandlerDefaultImpl;
import io.github.minguanqiu.mingle.svc.handler.SerialNumberGeneratorHandler;
import io.github.minguanqiu.mingle.svc.utils.JacksonUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for data bean.
 *
 * @author Qiu Guan Ming
 */
@Configuration
public class SvcDataConfiguration {

  /**
   * Create a new SvcDataProperties instance.
   *
   * @return return the SvcDataProperties instance.
   */
  @Bean
  @ConfigurationProperties("mingle.svc.dao")
  public SvcDataProperties dataProperties() {
    return new SvcDataProperties();
  }

  /**
   * Create a new DaoLogAspect instance.
   *
   * @return return the DaoLogAspect instance.
   */
  @Bean
  @ConditionalOnProperty(prefix = "mingle.svc.dao", name = "logging", havingValue = "true")
  public DaoLogAspect daoLogAspect(DaoLoggingHandler daoLoggingHandler,
      SerialNumberGeneratorHandler serialNumberGeneratorHandler) {
    return new DaoLogAspect(daoLoggingHandler, serialNumberGeneratorHandler);
  }

  /**
   * Create a new DaoLoggingHandler instance.
   *
   * @return return the DaoLoggingHandler instance.
   */
  @Bean
  @ConditionalOnMissingBean
  public DaoLoggingHandler daoLoggingHandler() {
    return new DaoLoggingHandlerDefaultImpl(dataLogJacksonUtils());
  }

  /**
   * Create a new JacksonUtils instance.
   *
   * @return return the JacksonUtils instance.
   */
  @Bean("dataLogJacksonUtils")
  @ConditionalOnMissingBean(name = "dataLogJacksonUtils")
  public JacksonUtils dataLogJacksonUtils() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    objectMapper.findAndRegisterModules();
    return new JacksonUtils(objectMapper);
  }

}
