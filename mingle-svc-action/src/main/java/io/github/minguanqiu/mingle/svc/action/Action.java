package io.github.minguanqiu.mingle.svc.action;

/**
 * Base class for all action
 *
 * <p>Action is a module,help uniform usage and logging.
 *
 * @param <R1> action request.
 * @param <R2> action response body.
 * @author Qiu Guan Ming
 */
public sealed interface Action<R1 extends ActionRequest, R2 extends ActionResponseBody> permits
    AbstractAction {

  /**
   * Execute action logic.
   *
   * @param request the action request.
   * @return return the action response.
   */
  ActionResponse<R2> doAction(R1 request);

  /**
   * Action type.
   *
   * @return return action type.
   */
  String getType();

}
