package io.github.minguanqiu.mingle.svc.filter;

import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Base class for all service filter
 *
 * @author Qiu Guan Ming
 */

public abstract class AbstractSvcFilter extends OncePerRequestFilter {

  /**
   * Service information.
   */
  protected final SvcInfo svcInfo;

  /**
   * Create a new AbstractSvcFilter instance.
   *
   * @param svcInfo the service information.
   */
  public AbstractSvcFilter(SvcInfo svcInfo) {
    this.svcInfo = svcInfo;
  }

}
