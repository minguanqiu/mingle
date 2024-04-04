package io.github.amings.mingle.svc.action.rest.exception;

import lombok.Getter;

/**
 * Exception for http code error when not include {@link io.github.amings.mingle.svc.action.rest.AbstractRestAction} successHttpCodeSet range
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
