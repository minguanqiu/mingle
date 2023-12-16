package io.github.amings.mingle.svc.action.exception;

import io.github.amings.mingle.svc.action.ActionResModel;
import lombok.Getter;

/**
 * @author Ming
 */

@Deprecated
@Getter
public class BreakActionException extends RuntimeException {

    private final String code;

    private final String desc;

    private final ActionResModel resModel;

    public BreakActionException(String code, String desc) {
        this.code = code;
        this.desc = desc;
        this.resModel = null;
    }

    public BreakActionException(String code, String desc, ActionResModel resModel) {
        this.code = code;
        this.desc = desc;
        this.resModel = resModel;
    }

}
