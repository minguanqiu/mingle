package io.github.minguanqiu.mingle.svc.action;

/**
 * Interface for action interceptor
 *
 * @author Qiu Guan Ming
 */
public interface ActionInterceptor {

  void intercept(Chain chain);

  /**
   * Interface for action chain
   *
   * @author Qiu Guan Ming
   */
  interface Chain {

    void proceed();

    AbstractAction<? extends ActionRequest, ? extends ActionResponseBody> actionTarget();

    ActionRequest request();

    ActionInfo actionInfo();

  }

}
