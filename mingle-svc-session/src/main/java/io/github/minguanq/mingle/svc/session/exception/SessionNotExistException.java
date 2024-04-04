package io.github.minguanq.mingle.svc.session.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Exception for session not exist
 *
 * @author Ming
 */
public class SessionNotExistException extends AuthenticationException {

    public SessionNotExistException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public SessionNotExistException(String msg) {
        super(msg);
    }
}
