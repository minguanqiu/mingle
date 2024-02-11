package io.github.amings.mingle.svc.action.exception;

import io.github.amings.mingle.svc.action.ActionResData;
import io.github.amings.mingle.svc.action.ActionResModel;
import io.github.amings.mingle.svc.action.ActionResponse;
import lombok.Getter;

/**
 * Exception for ActionAutoBreak
 *
 * @author Ming
 */

@Getter
public class ActionAutoBreakException extends RuntimeException {

    private final ActionResponse<? extends ActionResData, ? extends ActionResModel> actionResponse;

    public ActionAutoBreakException(ActionResponse<? extends ActionResData, ? extends ActionResModel> actionResponse) {
        this.actionResponse = actionResponse;
    }

}
