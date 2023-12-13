package io.github.amings.mingle.svc.handler.impl;

import io.github.amings.mingle.svc.handler.IPHandler;
import jakarta.servlet.http.HttpServletRequest;

/**
 * {@link IPHandler} impl
 *
 * @author Ming
 */

public class IPHandlerDefaultImpl implements IPHandler {

    @Override
    public String getIP(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getRemoteAddr();
    }
}
