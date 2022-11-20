package io.github.amings.mingle.svc.handler;

import io.github.amings.mingle.svc.handler.model.SvcBeginModel;
import io.github.amings.mingle.svc.handler.model.SvcEndModel;

/**
 * Implements to custom Svc logging method，must be a spring bean
 *
 * @author Ming
 */

public interface SvcLogHandler {

    void writeBeginLog(SvcBeginModel model);

    void writeEndLog(SvcEndModel model);

}
