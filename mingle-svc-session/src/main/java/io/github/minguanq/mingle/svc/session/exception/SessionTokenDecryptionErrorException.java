package io.github.minguanq.mingle.svc.session.exception;

/**
 * Exception for session token decryption error
 *
 * @author Ming
 */
public class SessionTokenDecryptionErrorException extends RuntimeException {
    public SessionTokenDecryptionErrorException(String message) {
        super(message);
    }
}
