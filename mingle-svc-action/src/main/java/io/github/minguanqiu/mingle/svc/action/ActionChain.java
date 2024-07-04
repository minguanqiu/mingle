package io.github.minguanqiu.mingle.svc.action;

import java.util.List;

/**
 * Interceptor chain for action.
 *
 * @author Qiu Guan Ming
 */
public class ActionChain implements ActionInterceptor.Chain {

  private final List<ActionInterceptor> actionInterceptors;
  private final AbstractAction<? extends ActionRequest, ? extends ActionResponseBody> action;
  private final ActionRequest actionRequest;
  private final ActionInfo actionInfo;
  private final int index;
  private int callTimes;

  /**
   * Create a new ActionChain instance.
   *
   * @param actionInterceptors the list of action interceptors.
   * @param action             the action object.
   * @param actionRequest      the action request.
   * @param actionInfo         the action information.
   * @param index              the interceptors index.
   */
  public ActionChain(List<ActionInterceptor> actionInterceptors,
      AbstractAction<? extends ActionRequest, ? extends ActionResponseBody> action,
      ActionRequest actionRequest,
      ActionInfo actionInfo, int index) {
    this.actionInterceptors = actionInterceptors;
    this.action = action;
    this.actionRequest = actionRequest;
    this.actionInfo = actionInfo;
    this.index = index;
  }

  @Override
  public void proceed() {
    if (index > actionInterceptors.size()) {
      throw new IllegalStateException("interceptor error");
    }
    callTimes++;
    if (callTimes == 1) { // prevention chain call twice
      actionInterceptors.get(index).intercept(
          new ActionChain(actionInterceptors, action, actionRequest, actionInfo,
              index + 1));
    } else {
      throw new IllegalStateException(
          "action interceptor " + this.actionInterceptors.get(index).getClass().getSimpleName()
              + " proceed() must call once");
    }
  }

  @Override
  public AbstractAction<? extends ActionRequest, ? extends ActionResponseBody> actionTarget() {
    return action;
  }

  @Override
  public ActionRequest request() {
    return actionRequest;
  }

  @Override
  public ActionInfo actionInfo() {
    return actionInfo;
  }

}
