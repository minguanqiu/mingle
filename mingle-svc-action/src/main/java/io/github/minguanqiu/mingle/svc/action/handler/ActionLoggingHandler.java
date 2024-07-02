package io.github.minguanqiu.mingle.svc.action.handler;

import io.github.minguanqiu.mingle.svc.action.AbstractAction;
import io.github.minguanqiu.mingle.svc.action.ActionInfo;

/**
 * @author Qiu Gaun Ming
 */
public interface ActionLoggingHandler {

  void writeBeginLog(AbstractAction action, ActionInfo actionInfo);

  void writeEndLog(AbstractAction action, ActionInfo actionInfo);

}
