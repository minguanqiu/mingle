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
import io.github.minguanqiu.mingle.svc.handler.*;
import io.github.minguanqiu.mingle.svc.handler.impl.*;
import io.github.minguanqiu.mingle.svc.handler.model.SvcFeature;
import io.github.minguanqiu.mingle.svc.json.view.Views;
import io.github.minguanqiu.mingle.svc.register.SvcFeatureRegister;
import io.github.minguanqiu.mingle.svc.register.SvcFeatureRegisterImpl;
import io.github.minguanqiu.mingle.svc.register.SvcRegister;
import io.github.minguanqiu.mingle.svc.utils.JacksonUtils;
import io.github.minguanqiu.mingle.svc.utils.SvcResUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
public class SvcConfiguration {

    @Bean
    @ConfigurationProperties("mingle.svc.properties")
    public SvcProperties svcPropertySource() {
        return new SvcProperties();
    }


    @Bean
    public SvcRegister svcBinderComponent(SvcProperties svcProperties, List<Service<?, ?>> services, RequestMappingHandlerMapping requestMappingHandlerMapping, SvcPathHandler svcPathHandler, List<SvcFeatureRegister<?>> svcFeatureRegister) {
        return new SvcRegister(svcProperties, services, requestMappingHandlerMapping, svcPathHandler, svcFeatureRegister);
    }

    @Bean
    public SvcFeatureRegister<SvcFeature> svcFeatureRegister(SvcProperties svcProperties, SvcFeatureHandler svcFeatureHandler) {
        return new SvcFeatureRegisterImpl(svcProperties, svcFeatureHandler);
    }

    /**
     * Create and return instance,implements {@link SvcLoggingHandler} to override default impl
     */
    @Bean
    @ConditionalOnMissingBean
    public SvcLoggingHandler svcLogHandler() {
        return new SvcLoggingHandlerDefaultImpl(svcLogJacksonUtils());
    }

    /**
     * Create and return instance,implements {@link SvcRequestBodyProcessHandler} to override default impl
     */
    @Bean
    @ConditionalOnMissingBean
    public SvcRequestBodyProcessHandler svcRequestBodyProcessHandler() {
        return new SvcSvcRequestBodyProcessHandlerDefaultImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public SvcFeatureHandler svcFeatureHandler() {
        return new SvcFeatureHandlerDefaultImpl();
    }

    /**
     * Svc return code mapping description map impl
     *
     * @return SvcMsgHandler
     * @see CodeMessageHandler
     */
    @Bean
    public CodeMessageHandler svcMsgHandler(List<CodeMessageListHandler> codeMessageListHandlers) {
        return new CodeMessageHandlerDefaultImpl(codeMessageListHandlers);
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

    @Bean
    public AllExceptionHandler allExceptionHandler(SvcInfo svcInfo) {
        return new AllExceptionHandler(svcInfo);
    }

    @Bean
    public BreakSvcProcessExceptionHandler breakSvcProcessExceptionHandler(SvcInfo svcInfo) {
        return new BreakSvcProcessExceptionHandler(svcInfo);
    }

    @Bean
    public BreakFilterProcessExceptionHandler breakFilterProcessExceptionHandler(SvcInfo svcInfo) {
        return new BreakFilterProcessExceptionHandler(svcInfo);
    }

}
