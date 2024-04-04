package io.github.minguanq.mingle.svc.session.exception;

/**
 * Exception for session token encryption error
 *
 * @author Ming
 */
public class SessionTokenEncryptionErrorException extends RuntimeException {
    public SessionTokenEncryptionErrorException(String message) {
        super(message);
    }
}
