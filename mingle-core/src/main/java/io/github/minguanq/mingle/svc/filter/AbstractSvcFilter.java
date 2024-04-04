package io.github.minguanq.mingle.svc.filter;

import io.github.minguanq.mingle.svc.exception.BreakFilterProcessException;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Base class for all service filter
 *
 * @author Ming
 */

public abstract class AbstractSvcFilter extends OncePerRequestFilter {

    protected final SvcInfo svcInfo;

    public AbstractSvcFilter(SvcInfo svcInfo) {
        this.svcInfo = svcInfo;
    }

    /**
     * Interrupt logic in filter
     *
     * @param code service response code
     * @throws BreakFilterProcessException if call this method
     */
    protected void breakFilterProcess(String code) {
        throw new BreakFilterProcessException(code, null);
    }

    /**
     * Interrupt logic in filter
     *
     * @param code service response code
     * @param msg  service response message
     * @throws BreakFilterProcessException if call this method
     */
    protected void breakFilterProcess(String code, String msg) {
        throw new BreakFilterProcessException(code, msg);
    }

}
