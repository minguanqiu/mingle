package io.github.amings.mingle.svc.handler;

import jakarta.servlet.http.HttpServletRequest;

/**
 * implements to custom get ip method，must be a spring bean
 *
 * @author Ming
 */

public interface IPHandler {

    String getIP(HttpServletRequest httpServletRequest);

}
