package io.github.minguanqiu.mingle.svc.action;

/**
 * Interceptor for execute action logic and process response
 *
 * @author Qiu Guan Ming
 */
public class ActionResponseInterceptor implements ActionInterceptor {

  @Override
  @SuppressWarnings("unchecked")
  public void intercept(Chain chain) {
    AbstractAction action = chain.actionTarget();
    ActionResponseBody responseBody = action.processLogic(chain.request(), chain.actionInfo());
    chain.actionInfo().setActionResponseBody(responseBody);
  }

}
