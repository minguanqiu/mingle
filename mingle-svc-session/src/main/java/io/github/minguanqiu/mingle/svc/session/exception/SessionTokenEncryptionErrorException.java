package io.github.minguanqiu.mingle.svc.session.exception;

/**
 * Exception for session token encryption error
 *
 * @author Ming
 */
public class SessionTokenEncryptionErrorException extends RuntimeException {

    public static final String MSG = "Session token encryption error";

    public SessionTokenEncryptionErrorException(Exception e) {
        super(MSG, e);
    }
}
