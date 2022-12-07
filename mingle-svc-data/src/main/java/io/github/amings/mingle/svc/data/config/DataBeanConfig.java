package io.github.amings.mingle.svc.data.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.github.amings.mingle.svc.data.aspect.DaoLogAspect;
import io.github.amings.mingle.svc.data.handler.DaoLogHandler;
import io.github.amings.mingle.svc.data.handler.impl.DaoLogHandlerDefaultImpl;
import io.github.amings.mingle.svc.json.view.Views;
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
    @ConditionalOnProperty(prefix = "mingle.svc.dao", name = "logging", havingValue = "enable")
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
        objectMapper.setVisibility(objectMapper.getVisibilityChecker().withVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY));
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.setConfig(objectMapper.getSerializationConfig().withView(Views.class));
        objectMapper.findAndRegisterModules();
        return new JacksonUtils(objectMapper);
    }

}
