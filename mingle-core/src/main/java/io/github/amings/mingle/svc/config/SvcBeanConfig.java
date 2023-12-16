package io.github.amings.mingle.svc.config;

import io.github.amings.mingle.svc.aspect.ActionLogAspect;
import io.github.amings.mingle.svc.handler.*;
import io.github.amings.mingle.svc.handler.impl.*;
import io.github.amings.mingle.utils.JacksonUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * bean configuration bundle
 *
 * @author Ming
 */

@Configuration
public class SvcBeanConfig {

    /**
     * implements {@link SvcLogHandler} to override default impl
     *
     * @return SvcLogHandler
     * @see SvcLogHandler
     */
    @Bean
    @ConditionalOnMissingBean
    public SvcLogHandler svcLogHandler() {
        return new SvcLogHandlerDefaultImpl();
    }

    /**
     * implements {@link ActionLogHandler} to override default impl
     *
     * @return ActionLogHandler
     * @see ActionLogHandler
     */
    @Bean
    @ConditionalOnMissingBean
    public ActionLogHandler actionLogHandler(@Qualifier("actionLogJacksonUtils")JacksonUtils jacksonUtils) {
        return new ActionLogHandlerDefaultImpl(jacksonUtils);
    }

    /**
     * custom request body process method
     * implements {@link PayLoadDecryptionHandler} to override default impl
     *
     * @return PayLoadHandler
     * @see PayLoadDecryptionHandler
     */
    @Bean
    @ConditionalOnMissingBean
    public PayLoadDecryptionHandler payLoadDecryptionHandler() {
        return new PayLoadDecryptionHandlerDefaultImpl();
    }

    /**
     * custom remote ip getter method
     * implements {@link IPHandler} to override default impl
     *
     * @return IPHandler
     * @see IPHandler
     */
    @Bean
    @ConditionalOnMissingBean
    public IPHandler ipHandler() {
        return new IPHandlerDefaultImpl();
    }

    /**
     * Svc return code mapping description map impl
     *
     * @return SvcMsgHandler
     * @see SvcMsgHandler
     */
    @Bean
    @Primary
    public SvcMsgHandler svcMsgHandler(List<SvcMsgListHandler> svcMsgListHandlers) {
        return new SvcMsgHandlerDefaultImpl(svcMsgListHandlers);
    }

    /**
     * @return ActionLogAspect
     * action logging aspect bean
     */
    @Bean
    @ConditionalOnProperty(prefix = "mingle.svc.action", name = "logging", havingValue = "enable")
    public ActionLogAspect actionLogAspect() {
        return new ActionLogAspect();
    }

    /**
     * @return SvcResModelHandler
     * Svc response model bean
     */
    @Bean
    @ConditionalOnMissingBean
    public SvcResModelHandler svcResModelHandler() {
        return new SvcResModelDefaultImpl();
    }

}
