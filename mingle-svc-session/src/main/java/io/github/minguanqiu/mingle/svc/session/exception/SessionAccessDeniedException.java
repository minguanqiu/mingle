package io.github.minguanqiu.mingle.svc.session.exception;

import org.springframework.security.access.AccessDeniedException;

/**
 * Exception for session access denied
 *
 * @author Qiu Guan Ming
 */
public class SessionAccessDeniedException extends AccessDeniedException {

  public SessionAccessDeniedException(String msg) {
    super(msg);
  }

  public SessionAccessDeniedException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
