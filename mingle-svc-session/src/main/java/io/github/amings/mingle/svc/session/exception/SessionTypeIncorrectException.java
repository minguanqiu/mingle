package io.github.amings.mingle.svc.session.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author Ming
 */
public class SessionTypeIncorrectException extends AuthenticationException {

    public SessionTypeIncorrectException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public SessionTypeIncorrectException(String msg) {
        super(msg);
    }

}
