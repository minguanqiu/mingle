package io.github.minguanq.mingle.svc.session.exception;

/**
 * Exception for session token decryption error
 *
 * @author Ming
 */
public class SessionTokenDecryptionErrorException extends RuntimeException {

    public static final String MSG = "Session token decryption error";

    public SessionTokenDecryptionErrorException() {
        super(MSG);
    }

}
