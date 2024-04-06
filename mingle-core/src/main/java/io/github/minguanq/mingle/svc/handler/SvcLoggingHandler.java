package io.github.minguanq.mingle.svc.handler;

import io.github.minguanq.mingle.svc.filter.SvcInfo;

/**
 * Handler for service logging logic
 *
 * @author Ming
 */

public interface SvcLoggingHandler {

    void writeBeginLog(SvcInfo svcInfo);

    void writeEndLog(SvcInfo svcInfo);

}
