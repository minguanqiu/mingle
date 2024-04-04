package io.github.minguanq.mingle.svc.data.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.github.minguanq.mingle.svc.data.aspect.DaoLogAspect;
import io.github.minguanq.mingle.svc.data.handler.DaoLogHandler;
import io.github.minguanq.mingle.svc.data.handler.impl.DaoLogHandlerDefaultImpl;
import io.github.minguanq.mingle.svc.handler.SerialNumberGeneratorHandler;
import io.github.minguanq.mingle.svc.utils.JacksonUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for data bean
 *
 * @author Ming
 */

@Configuration
public class DataBeanConfig {

    @Bean
    @ConditionalOnProperty(prefix = "mingle.svc.dao", name = "logging", havingValue = "enable")
    public DaoLogAspect daoLogAspect(SerialNumberGeneratorHandler serialNumberGeneratorHandler) {
        return new DaoLogAspect(daoLogHandler(), serialNumberGeneratorHandler);
    }

    @Bean
    @ConditionalOnMissingBean
    public DaoLogHandler daoLogHandler() {
        return new DaoLogHandlerDefaultImpl(dataLogJacksonUtils());
    }

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
