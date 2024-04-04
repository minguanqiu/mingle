package io.github.minguanq.mingle.svc.session.exception;

/**
 * Exception for session header missing
 *
 * @author Ming
 */
public class SessionHeaderMissingException extends RuntimeException {

    public SessionHeaderMissingException(String message) {
        super(message);
    }
}
