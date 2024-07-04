package io.github.minguanqiu.mingle.svc.action.exception.handler;

import io.github.minguanqiu.mingle.svc.action.exception.handler.abs.AbstractActionExceptionHandler;
import io.github.minguanqiu.mingle.svc.action.exception.handler.model.ActionExceptionModel;

/**
 * Default handler will catch {@link Exception} or unknown exception.
 *
 * @author Qiu Guan Ming
 */
public class AllActionExceptionHandler extends AbstractActionExceptionHandler<Exception> {

  @Override
  public ActionExceptionModel handle(Exception ex, ActionExceptionModel actionExceptionModel) {
    actionExceptionModel.setCode("error");
    actionExceptionModel.setMsg(ex.getMessage());
    return actionExceptionModel;
  }
}
