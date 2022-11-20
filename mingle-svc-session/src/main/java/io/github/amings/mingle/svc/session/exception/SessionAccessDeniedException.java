package io.github.amings.mingle.svc.session.exception;

import org.springframework.security.access.AccessDeniedException;

/**
 * @author Ming
 */
public class SessionAccessDeniedException extends AccessDeniedException {

    public SessionAccessDeniedException(String msg) {
        super(msg);
    }

    public SessionAccessDeniedException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
