package io.github.minguanqiu.mingle.svc.action.rest.exception;

import io.github.minguanqiu.mingle.svc.action.rest.AbstractRestAction;
import lombok.Getter;

/**
 * Exception for http code error when not include {@link AbstractRestAction} successHttpCodeSet range
 *
 * @author Ming
 */

@Getter
public class HttpCodeErrorException extends RuntimeException {

    private final int code;

    public HttpCodeErrorException(int code, String message) {
        super(message);
        this.code = code;
    }

}
