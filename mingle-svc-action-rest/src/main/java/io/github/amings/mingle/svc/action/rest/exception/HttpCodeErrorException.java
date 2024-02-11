package io.github.amings.mingle.svc.action.rest.exception;

import lombok.Getter;

/**
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
