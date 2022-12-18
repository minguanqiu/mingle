package io.github.amings.mingle.svc.session.exception;

/**
 * @author Ming
 */
public class JwtDecryptionFailException extends RuntimeException {
    public JwtDecryptionFailException(String message) {
        super(message);
    }
}
