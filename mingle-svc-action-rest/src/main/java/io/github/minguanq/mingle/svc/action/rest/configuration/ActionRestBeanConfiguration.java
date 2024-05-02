package io.github.minguanq.mingle.svc.action.rest.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.github.minguanq.mingle.svc.action.rest.configuration.properties.RestActionProperties;
import io.github.minguanq.mingle.svc.action.rest.handler.RestClientHandler;
import io.github.minguanq.mingle.svc.action.rest.handler.impl.RestClientDefaultHandler;
import io.github.minguanq.mingle.svc.action.rest.json.view.Views;
import io.github.minguanq.mingle.svc.action.rest.utils.RestActionJacksonUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for action rest bean
 *
 * @author Ming
 */

@Configuration
public class ActionRestBeanConfiguration {

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
    @ConditionalOnMissingBean
    public RestActionJacksonUtils restActionJacksonUtils() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.setConfig(objectMapper.getSerializationConfig().withView(Views.class));
        return new RestActionJacksonUtils(objectMapper);
    }

}
