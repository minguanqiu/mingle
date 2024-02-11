package io.github.amings.mingle.svc.action.handler;

import io.github.amings.mingle.svc.action.*;

/**
 * Implements to custom action logging method，must be a spring bean
 *
 * @author Ming
 */

public interface ActionLogHandler {

    void writeBeginLog(AbstractAction<? extends ActionReqModel, ? extends ActionResData, ? extends ActionResModel> action, ActionReqModel actionReqModel);

    void writeEndLog(AbstractAction<? extends ActionReqModel, ? extends ActionResData, ? extends ActionResModel> action, ActionResponse<? extends ActionResData, ? extends ActionResModel> actionResponse);

}
