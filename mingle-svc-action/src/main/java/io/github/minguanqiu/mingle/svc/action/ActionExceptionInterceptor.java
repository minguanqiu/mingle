package io.github.minguanqiu.mingle.svc.action;

import io.github.minguanqiu.mingle.svc.action.exception.handler.model.ActionExceptionModel;
import io.github.minguanqiu.mingle.svc.action.exception.resolver.ActionExceptionHandlerResolver;

/**
 * @author Qiu Gaun Ming
 */
public class ActionExceptionInterceptor implements ActionInterceptor {

  private final ActionExceptionHandlerResolver actionExceptionHandlerResolver;

  public ActionExceptionInterceptor(ActionExceptionHandlerResolver actionExceptionHandlerResolver) {
    this.actionExceptionHandlerResolver = actionExceptionHandlerResolver;
  }

  @Override
  public void intercept(Chain chain) {
    try {
      chain.proceed();
    } catch (Exception e) {
      if (actionExceptionHandlerResolver == null) {
        throw e;
      }
      ActionExceptionModel actionExceptionModel = actionExceptionHandlerResolver.resolver(e);
      chain.actionInfo().setCode(actionExceptionModel.getCode());
      chain.actionInfo().setMsg(actionExceptionModel.getMsg());
    }

  }

}
