package io.github.minguanqiu.mingle.svc.handler;

import io.github.minguanqiu.mingle.svc.filter.SvcInfo;

/**
 * Handler for service logging logic.
 *
 * @author Qiu Guan Ming
 */

public interface SvcLoggingHandler {

  /**
   * Pre-processing logging for service.
   *
   * @param svcInfo the service information.
   */
  void writeBeginLog(SvcInfo svcInfo);

  /**
   * Post-processing logging for service.
   *
   * @param svcInfo the service information.
   */
  void writeEndLog(SvcInfo svcInfo);

}
