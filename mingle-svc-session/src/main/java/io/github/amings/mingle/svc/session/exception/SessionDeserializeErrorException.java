package io.github.amings.mingle.svc.session.exception;

/**
 * Exception for session deserialize error
 *
 * @author Ming
 */
public class SessionDeserializeErrorException extends RuntimeException {

    public SessionDeserializeErrorException(String message) {
        super(message);
    }
}
