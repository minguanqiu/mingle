package io.github.minguanqiu.mingle.svc.action.exception;

import io.github.minguanqiu.mingle.svc.action.ActionResponse;
import io.github.minguanqiu.mingle.svc.action.ActionResponseBody;
import lombok.Getter;

/**
 * Exception for when action error will auto break logic
 *
 * @author Qiu Guan Ming
 */

@Getter
public class ActionAutoBreakException extends RuntimeException {

  private final ActionResponse<? extends ActionResponseBody> actionResponse;

  public ActionAutoBreakException(ActionResponse<? extends ActionResponseBody> actionResponse) {
    this.actionResponse = actionResponse;
  }

}
