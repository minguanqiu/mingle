package io.github.minguanq.mingle.svc.exception;

/**
 * Exception for service not found
 *
 * @author Ming
 */
public class SvcNotFoundException extends RuntimeException{

    public SvcNotFoundException(String msg) {
       super(msg);
    }

}
