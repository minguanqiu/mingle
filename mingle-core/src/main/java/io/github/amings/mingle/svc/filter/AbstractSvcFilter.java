package io.github.amings.mingle.svc.filter;

import io.github.amings.mingle.svc.configuration.properties.SvcProperties;
import io.github.amings.mingle.svc.exception.BreakFilterProcessException;
import io.github.amings.mingle.svc.handler.SvcMsgHandler;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * base class for all custom Svc filter
 *
 * @author Ming
 */

public abstract class AbstractSvcFilter extends OncePerRequestFilter {

    protected final SvcInfo svcInfo;
    protected final SvcMsgHandler svcMsgHandler;
    protected final SvcProperties svcProperties;

    public AbstractSvcFilter(SvcInfo svcInfo, SvcMsgHandler svcMsgHandler, SvcProperties svcProperties) {
        this.svcInfo = svcInfo;
        this.svcMsgHandler = svcMsgHandler;
        this.svcProperties = svcProperties;
    }

    /**
     * @param code system code
     *             break filter method
     */
    protected void breakFilterProcess(String code) {
        throw new BreakFilterProcessException(code, svcMsgHandler.getMsg(svcProperties.getMsgType(), code));
    }

    /**
     * @param code system code
     * @param desc desc
     *             break filter method
     */
    protected void breakFilterProcess(String code, String desc) {
        throw new BreakFilterProcessException(code, desc);
    }

}
