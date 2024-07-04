package io.github.minguanqiu.mingle.svc.action;

/**
 * Interface for action interceptor.
 *
 * @author Qiu Guan Ming
 */
public interface ActionInterceptor {

  /**
   * Execute interceptor logic.
   *
   * @param chain the action interceptor chain.
   */
  void intercept(Chain chain);

  /**
   * Interface for action chain.
   *
   * @author Qiu Guan Ming
   */
  interface Chain {

    /**
     * Execute next interceptor process logic.
     */
    void proceed();

    /**
     * Get action target object.
     *
     * @return return the action object.
     */
    AbstractAction<? extends ActionRequest, ? extends ActionResponseBody> actionTarget();

    /**
     * Get action request.
     *
     * @return the action request.
     */
    ActionRequest request();

    /**
     * Get action information.
     *
     * @return the action information.
     */
    ActionInfo actionInfo();

  }

}
