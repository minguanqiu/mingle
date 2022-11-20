package io.github.amings.mingle.svc.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author Ming
 */
public class SvcAuthenticationException extends AuthenticationException {

    public SvcAuthenticationException(String msg) {
        super(msg);
    }

    public SvcAuthenticationException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
