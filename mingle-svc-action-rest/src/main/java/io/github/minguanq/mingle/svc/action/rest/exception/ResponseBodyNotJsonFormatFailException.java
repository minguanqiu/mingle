package io.github.minguanq.mingle.svc.action.rest.exception;

/**
 * Exception for response body not a json format
 *
 * @author Ming
 */

public class ResponseBodyNotJsonFormatFailException extends RuntimeException {

    public ResponseBodyNotJsonFormatFailException(String message) {
        super(message);
    }
}
