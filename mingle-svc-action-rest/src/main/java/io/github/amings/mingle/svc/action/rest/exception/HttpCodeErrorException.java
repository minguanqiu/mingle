package io.github.amings.mingle.svc.action.rest.exception;

import lombok.Getter;

/**
 * @author Ming
 */

public class HttpCodeErrorException extends RuntimeException {

    @Getter
    private final int code;

    public HttpCodeErrorException(int code, String message) {
        super(message);
        this.code = code;
    }

}
