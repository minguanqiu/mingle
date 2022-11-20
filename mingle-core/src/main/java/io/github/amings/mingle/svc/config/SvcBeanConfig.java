package io.github.amings.mingle.svc.config;

import io.github.amings.mingle.svc.aspect.ActionLogAspect;
import io.github.amings.mingle.svc.handler.ActionLogHandler;
import io.github.amings.mingle.svc.handler.IPHandler;
import io.github.amings.mingle.svc.handler.PayLoadDecryptionHandler;
import io.github.amings.mingle.svc.handler.SvcLogHandler;
import io.github.amings.mingle.svc.handler.SvcMsgHandler;
import io.github.amings.mingle.svc.handler.SvcResModelHandler;
import io.github.amings.mingle.svc.handler.impl.ActionLogHandlerDefalutImpl;
import io.github.amings.mingle.svc.handler.impl.IPHandlerDefaultImpl;
import io.github.amings.mingle.svc.handler.impl.PayLoadDecryptionHandlerDefaultImpl;
import io.github.amings.mingle.svc.handler.impl.SvcLogHandlerDefaultImpl;
import io.github.amings.mingle.svc.handler.impl.SvcMsgHandlerDefaultImpl;
import io.github.amings.mingle.svc.handler.impl.SvcResModelDefaultImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

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
    public ActionLogHandler actionLogHandler() {
        return new ActionLogHandlerDefalutImpl();
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
    public SvcMsgHandler svcMsgHandler() {
        return new SvcMsgHandlerDefaultImpl();
    }

    /**
     * @return ActionLogAspect
     * action logging aspect bean
     */
    @Bean
    @ConditionalOnProperty(prefix = "mingle.svc.logging", name = "action", havingValue = "enable")
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
