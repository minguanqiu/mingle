package io.github.amings.mingle.svc.exception;

import lombok.Getter;

/**
 * for Svc Filter break logic Exception
 *
 * @author Ming
 */
@Getter
public class BreakFilterProcessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String code;

    private final String desc;

    /**
     * @param code response code
     * @param desc response desc
     */
    public BreakFilterProcessException(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
