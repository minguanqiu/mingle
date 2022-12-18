package io.github.amings.mingle.svc.session.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author Ming
 */
public class SessionNotFoundException extends AuthenticationException {

    public SessionNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public SessionNotFoundException(String msg) {
        super(msg);
    }
}
