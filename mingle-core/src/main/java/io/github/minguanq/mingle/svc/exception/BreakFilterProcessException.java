package io.github.minguanq.mingle.svc.exception;

import lombok.Getter;

import java.io.Serial;

/**
 * Exception for break filter process logic
 *
 * @author Ming
 */
@Getter
public class BreakFilterProcessException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String code;

    private final String msg;

    /**
     * @param code response code
     * @param msg response desc
     */
    public BreakFilterProcessException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
