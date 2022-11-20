package io.github.amings.mingle.svc.filter;

import io.github.amings.mingle.svc.exception.BreakFilterProcessException;
import io.github.amings.mingle.svc.handler.SvcMsgHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * base class for all custom Svc filter
 *
 * @author Ming
 */

public abstract class AbstractSvcFilter extends OncePerRequestFilter {

    @Autowired
    protected SvcInfo svcInfo;
    @Autowired
    protected SvcMsgHandler svcMsgHandler;

    /**
     * @param code system code
     * break filter method
     */
    protected void breakFilterProcess(String code) throws BreakFilterProcessException {
        throw new BreakFilterProcessException(code, svcMsgHandler.getMsg(code));
    }

}
