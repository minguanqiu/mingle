package io.github.amings.mingle.svc.data.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.github.amings.mingle.svc.data.aspect.DaoLogAspect;
import io.github.amings.mingle.svc.data.handler.DaoLogHandler;
import io.github.amings.mingle.svc.data.handler.impl.DaoLogHandlerDefaultImpl;
import io.github.amings.mingle.utils.JacksonUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Bean configuration
 *
 * @author Ming
 */

@Configuration
public class DataBeanConfig {

    @Bean
    @ConditionalOnProperty(prefix = "mingle.svc.logging", name = "dao", havingValue = "enable")
    public DaoLogAspect daoLogAspect() {
        return new DaoLogAspect();
    }

    @Bean
    @ConditionalOnMissingBean(DaoLogHandler.class)
    public DaoLogHandler daoLogHandler() {
        return new DaoLogHandlerDefaultImpl();
    }

    @Bean("dataLogJacksonUtils")
    @ConditionalOnMissingBean(name = "dataLogJacksonUtils")
    public JacksonUtils dataLogJacksonUtils() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        return new JacksonUtils(objectMapper);
    }

}
