package io.github.minguanqiu.mingle.svc.handler;

import io.github.minguanqiu.mingle.svc.filter.SvcInfo;

/**
 * Handler for service logging logic
 *
 * @author Qiu Guan Ming
 */

public interface SvcLoggingHandler {

  void writeBeginLog(SvcInfo svcInfo);

  void writeEndLog(SvcInfo svcInfo);

}
