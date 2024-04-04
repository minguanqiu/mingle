package io.github.amings.mingle.svc.action.rest.exception;

/**
 * Exception for client error
 *
 * @author Ming
 */

public class ClientErrorException extends RuntimeException {

    public ClientErrorException(String message, Throwable cause) {
        super(message, cause);
    }

}
