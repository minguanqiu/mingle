package io.github.amings.mingle.svc.session.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author Ming
 */
public class SessionKickException extends AuthenticationException {

    public SessionKickException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public SessionKickException(String msg) {
        super(msg);
    }
}
