package io.github.minguanqiu.mingle.svc.action.exception;

import io.github.minguanqiu.mingle.svc.action.ActionResponse;
import io.github.minguanqiu.mingle.svc.action.ActionResponseBody;
import lombok.Getter;

/**
 * Exception for action error will auto break logic.
 *
 * @author Qiu Guan Ming
 */

@Getter
public class ActionAutoBreakException extends RuntimeException {

  /**
   * Action response.
   *
   * @return return an action response.
   */
  private final ActionResponse<? extends ActionResponseBody> actionResponse;

  /**
   * Create a new ActionAutoBreakException instance.
   *
   * @param actionResponse the action response.
   */
  public ActionAutoBreakException(ActionResponse<? extends ActionResponseBody> actionResponse) {
    this.actionResponse = actionResponse;
  }

}
