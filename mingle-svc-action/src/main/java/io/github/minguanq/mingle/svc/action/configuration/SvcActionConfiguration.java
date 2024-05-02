package io.github.minguanq.mingle.svc.action.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.github.minguanq.mingle.svc.action.configuration.properties.SvcActionProperties;
import io.github.minguanq.mingle.svc.action.exception.handler.ActionAutoBreakExceptionHandler;
import io.github.minguanq.mingle.svc.action.exception.handler.AllActionExceptionHandler;
import io.github.minguanq.mingle.svc.action.exception.handler.abs.AbstractActionExceptionHandler;
import io.github.minguanq.mingle.svc.action.exception.resolver.ActionExceptionHandlerResolver;
import io.github.minguanq.mingle.svc.filter.SvcInfo;
import io.github.minguanq.mingle.svc.handler.CodeMessageHandler;
import io.github.minguanq.mingle.svc.json.view.Views;
import io.github.minguanq.mingle.svc.utils.JacksonUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration for action
 *
 * @author Ming
 */
@Configuration
public class SvcActionConfiguration {

    @Bean
    @ConfigurationProperties("mingle.svc.action")
    public SvcActionProperties svcActionProperties() {
        return new SvcActionProperties();
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
    public ActionExceptionHandlerResolver actionExceptionHandlerResolver(List<AbstractActionExceptionHandler<?>> abstractExceptionHandlers) {
        return new ActionExceptionHandlerResolver(abstractExceptionHandlers);
    }

    @Bean
    public ActionAutoBreakExceptionHandler actionAutoBreakExceptionHandler(SvcInfo svcInfo, CodeMessageHandler codeMessageHandler) {
        return new ActionAutoBreakExceptionHandler(svcInfo, codeMessageHandler);
    }

    @Bean
    public AllActionExceptionHandler allActionExceptionHandler() {
        return new AllActionExceptionHandler();
    }

}
