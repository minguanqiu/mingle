package io.github.amings.mingle.svc.exception;

import io.github.amings.mingle.svc.action.ActionResData;
import lombok.Getter;

/**
 * for Svc Filter break logic Exception
 *
 * @author Ming
 */

public class BreakFilterProcessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    @Getter
    private final String code;

    @Getter
    private final String desc;

    /**
     * @param code response code
     * @param desc response desc
     */
    public BreakFilterProcessException(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * @param resData Action response data
     */
    public BreakFilterProcessException(ActionResData<?> resData) {
        this.code = resData.getCode();
        this.desc = resData.getDesc();
    }

}
