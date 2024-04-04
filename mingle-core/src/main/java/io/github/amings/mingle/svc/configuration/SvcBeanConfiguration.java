package io.github.amings.mingle.svc.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.github.amings.mingle.svc.Service;
import io.github.amings.mingle.svc.component.SvcBinderComponent;
import io.github.amings.mingle.svc.configuration.properties.SvcProperties;
import io.github.amings.mingle.svc.exception.handler.abs.AbstractExceptionHandler;
import io.github.amings.mingle.svc.exception.handler.resolver.ExceptionHandlerResolver;
import io.github.amings.mingle.svc.handler.*;
import io.github.amings.mingle.svc.handler.impl.*;
import io.github.amings.mingle.svc.json.view.Views;
import io.github.amings.mingle.svc.utils.JacksonUtils;
import io.github.amings.mingle.svc.utils.SvcResUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;

/**
 * Configuration for service bean
 *
 * @author Ming
 */
@Configuration
public class SvcBeanConfiguration {

    @Bean
    public SvcBinderComponent svcBinderComponent(SvcProperties svcProperties, List<Service<?, ?>> services, RequestMappingHandlerMapping requestMappingHandlerMapping, SvcPathHandler svcPathHandler) {
        return new SvcBinderComponent(svcProperties, services, requestMappingHandlerMapping, svcPathHandler);
    }

    /**
     * Create and return instance,implements {@link SvcLogHandler} to override default impl
     */
    @Bean
    @ConditionalOnMissingBean
    public SvcLogHandler svcLogHandler() {
        return new SvcLogHandlerDefaultImpl(svcLogJacksonUtils());
    }

    /**
     * Create and return instance,implements {@link SvcRequestBodyProcessHandler} to override default impl
     */
    @Bean
    @ConditionalOnMissingBean
    public SvcRequestBodyProcessHandler payLoadDecryptionHandler() {
        return new SvcSvcRequestBodyProcessHandlerDefaultImpl();
    }

    /**
     * Svc return code mapping description map impl
     *
     * @return SvcMsgHandler
     * @see SvcMsgHandler
     */
    @Bean
    public SvcMsgHandler svcMsgHandler(List<SvcMsgListHandler> svcMsgListHandlers) {
        return new SvcMsgHandlerDefaultImpl(svcMsgListHandlers);
    }

    /**
     * @return SvcResModelHandler
     * Svc response model bean
     */
    @Bean
    @ConditionalOnMissingBean
    public SvcResponseHandler svcResModelHandler() {
        return new SvcResponseDefaultImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public SvcPathHandler svcPathHandler() {
        return new SvcPathHandlerDefaultImpl();
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean(name = "svcJacksonUtils")
    public JacksonUtils svcJacksonUtils() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        return new JacksonUtils(objectMapper);
    }

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

    @Bean
    @ConditionalOnMissingBean
    public SerialNumberGeneratorHandler svcSerialNumberGenerator() {
        return new SerialNumberGeneratorHandlerDefaultImpl();
    }

    @Bean
    public ExceptionHandlerResolver exceptionHandlerResolver(List<AbstractExceptionHandler<?>> abstractExceptionHandlers) {
        return new ExceptionHandlerResolver(abstractExceptionHandlers);
    }

    @Bean
    public SvcResUtils svcResUtils(SvcResponseHandler svcResponseHandler, JacksonUtils jacksonUtils) {
        return new SvcResUtils(svcResponseHandler, jacksonUtils);
    }

}
