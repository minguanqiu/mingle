package io.github.amings.mingle.svc.session.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Authentication fail exception
 *
 * @author Ming
 */

@Deprecated
public class SvcAuthenticationFailException extends AuthenticationException {

    public SvcAuthenticationFailException(String msg) {
        super(msg);
    }

    public SvcAuthenticationFailException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

}
