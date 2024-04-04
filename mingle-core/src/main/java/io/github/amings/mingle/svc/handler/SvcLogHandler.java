package io.github.amings.mingle.svc.handler;

import io.github.amings.mingle.svc.filter.SvcInfo;

/**
 * Handler for service logging logic
 *
 * @author Ming
 */

public interface SvcLogHandler {

    void writeBeginLog(SvcInfo svcInfo);

    void writeEndLog(SvcInfo svcInfo);

}
