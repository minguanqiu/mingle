package io.github.amings.mingle.svc.action.rest.exception;

/**
 * BreakRestActionException
 *
 * @author Ming
 */

@Deprecated
public class BreakRestActionException extends Exception {

    private final String code;

    private final String desc;

    public BreakRestActionException(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
