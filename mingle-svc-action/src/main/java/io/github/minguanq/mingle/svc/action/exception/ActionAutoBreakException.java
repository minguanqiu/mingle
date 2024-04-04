package io.github.minguanq.mingle.svc.action.exception;

import io.github.minguanq.mingle.svc.action.ActionResponse;
import io.github.minguanq.mingle.svc.action.ActionResponseBody;
import lombok.Getter;

/**
 * Exception for when action error will auto break logic
 *
 * @author Ming
 */

@Getter
public class ActionAutoBreakException extends RuntimeException {

    private final ActionResponse<? extends ActionResponseBody> actionResponse;

    public ActionAutoBreakException(ActionResponse<? extends ActionResponseBody> actionResponse) {
        this.actionResponse = actionResponse;
    }

}
