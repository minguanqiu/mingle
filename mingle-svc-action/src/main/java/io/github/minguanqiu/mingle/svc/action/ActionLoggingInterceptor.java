package io.github.minguanqiu.mingle.svc.action;

import io.github.minguanqiu.mingle.svc.action.handler.ActionLoggingHandler;

/**
 * @author Qiu Gaun Ming
 */
public class ActionLoggingInterceptor implements ActionInterceptor {

  private final ActionLoggingHandler actionLoggingHandler;

  public ActionLoggingInterceptor(ActionLoggingHandler actionLoggingHandler) {
    this.actionLoggingHandler = actionLoggingHandler;
  }

  @Override
  public void intercept(Chain chain) {
    actionLoggingHandler.writeBeginLog(chain.actionTarget(), chain.actionInfo());
    chain.proceed();
    actionLoggingHandler.writeEndLog(chain.actionTarget(), chain.actionInfo());
  }

}
