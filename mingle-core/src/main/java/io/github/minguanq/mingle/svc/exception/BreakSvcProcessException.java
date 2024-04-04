package io.github.minguanq.mingle.svc.exception;

import io.github.minguanq.mingle.svc.SvcResponse;
import lombok.Getter;

/**
 * Exception for break service process logic
 *
 * @author Ming
 */

@Getter
public class BreakSvcProcessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String code;

    private final String msg;

    private SvcResponse svcResponse = new SvcResponse();

    /**
     * @param code        response code
     * @param msg        response msg
     * @param svcResponse Svc response model
     */
    public BreakSvcProcessException(String code, String msg, SvcResponse svcResponse) {
        this.code = code;
        this.msg = msg;
        if(svcResponse != null) {
            this.svcResponse = svcResponse;
        }
    }

}
