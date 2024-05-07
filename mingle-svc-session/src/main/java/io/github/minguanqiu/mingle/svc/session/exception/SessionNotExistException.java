package io.github.minguanqiu.mingle.svc.session.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Exception for session not exist
 *
 * @author Ming
 */
public class SessionNotExistException extends AuthenticationException {

    public static final String MSG = "Session not exist";

    public SessionNotExistException() {
        super(MSG);
    }
}
