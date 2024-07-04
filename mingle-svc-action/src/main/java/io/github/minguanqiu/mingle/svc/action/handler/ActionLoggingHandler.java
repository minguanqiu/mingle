package io.github.minguanqiu.mingle.svc.action.handler;

import io.github.minguanqiu.mingle.svc.action.AbstractAction;
import io.github.minguanqiu.mingle.svc.action.ActionInfo;

/**
 * Handler for action logging logic.
 *
 * @author Qiu Gaun Ming
 */
public interface ActionLoggingHandler {

  /**
   * Pre-processing logging for action.
   *
   * @param action     the action object.
   * @param actionInfo the action information.
   */
  void writeBeginLog(AbstractAction action, ActionInfo actionInfo);

  /**
   * Post-processing logging for action.
   *
   * @param action     the action object.
   * @param actionInfo the action information.
   */
  void writeEndLog(AbstractAction action, ActionInfo actionInfo);

}
