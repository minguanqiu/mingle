package io.github.amings.mingle.svc.action.rest.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.github.amings.mingle.svc.action.rest.handler.RestClientHandler;
import io.github.amings.mingle.svc.action.rest.handler.impl.RestClientDefaultHandler;
import io.github.amings.mingle.svc.action.rest.json.view.Views;
import io.github.amings.mingle.utils.JacksonUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Ming
 */

@Configuration
public class ActionRestBeanConfig {

    @Bean
    @ConditionalOnMissingBean
    public RestClientHandler restClientHandler() {
        return new RestClientDefaultHandler();
    }

    @Bean("restActionJacksonUtils")
    @ConditionalOnMissingBean(name = "restActionJacksonUtils")
    public JacksonUtils restActionJacksonUtils() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(objectMapper.getVisibilityChecker().withVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY));
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.setConfig(objectMapper.getSerializationConfig().withView(Views.class));
        return new JacksonUtils(objectMapper);
    }

}
