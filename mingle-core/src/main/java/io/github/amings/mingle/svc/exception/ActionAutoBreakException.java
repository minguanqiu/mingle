package io.github.amings.mingle.svc.exception;

import io.github.amings.mingle.svc.action.ActionResData;
import io.github.amings.mingle.svc.action.ActionResModel;
import lombok.Getter;

/**
 * Exception for ActionAutoBreak
 *
 * @author Ming
 */

public class ActionAutoBreakException extends RuntimeException {

    @Getter
    private final ActionResData<? extends ActionResModel> actionResData;

    public ActionAutoBreakException(ActionResData<? extends ActionResModel> actionResData) {
        this.actionResData = actionResData;
    }

}
