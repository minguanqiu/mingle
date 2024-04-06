package io.github.minguanq.mingle.svc.exception;

/**
 * Exception for service not found
 *
 * @author Ming
 */
public class SvcNotFoundException extends RuntimeException {

    public static final String MSG = "Service not found";

    public SvcNotFoundException() {
        super(MSG);
    }

}
