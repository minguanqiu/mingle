package io.github.amings.mingle.svc.exception;

/**
 * Exception for request body not a json format
 *
 * @author Ming
 */
public class ReqBodyNotJsonFormatException extends RuntimeException {

    public ReqBodyNotJsonFormatException(String message) {
        super(message);
    }
}
