package io.github.amings.mingle.svc.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.github.amings.mingle.svc.json.view.Views;
import io.github.amings.mingle.utils.JacksonUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * custom {@link MappingJackson2HttpMessageConverter} for Svc response pojo mapping
 *
 * @author Ming
 */

@Configuration
public class JacksonConfiguration {

    @Bean
    @ConditionalOnMissingBean(JacksonUtils.class)
    public JacksonUtils jacksonUtils() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        return new JacksonUtils(objectMapper);
    }

    @Bean("svcLogJacksonUtils")
    @ConditionalOnMissingBean(name = "svcLogJacksonUtils")
    public JacksonUtils svcLogJacksonUtils() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.setConfig(objectMapper.getSerializationConfig().withView(Views.class));
        objectMapper.findAndRegisterModules();
        return new JacksonUtils(objectMapper);
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

}