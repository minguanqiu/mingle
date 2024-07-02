package io.github.minguanqiu.mingle.svc.filter;

import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Base class for all service filter
 *
 * @author Qiu Guan Ming
 */

public abstract class AbstractSvcFilter extends OncePerRequestFilter {

  protected final SvcInfo svcInfo;

  public AbstractSvcFilter(SvcInfo svcInfo) {
    this.svcInfo = svcInfo;
  }

}
