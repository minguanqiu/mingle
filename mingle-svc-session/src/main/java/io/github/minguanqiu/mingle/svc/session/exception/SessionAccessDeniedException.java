package io.github.minguanqiu.mingle.svc.session.exception;

import org.springframework.security.access.AccessDeniedException;

/**
 * Exception for session access denied.
 *
 * @author Qiu Guan Ming
 */
public class SessionAccessDeniedException extends AccessDeniedException {

  /**
   * Create a new SessionAccessDeniedException instance.
   *
   * @param msg the exception message.
   */
  public SessionAccessDeniedException(String msg) {
    super(msg);
  }

  /**
   * Create a new SessionAccessDeniedException instance.
   *
   * @param msg   the exception message.
   * @param cause the cause exception.
   */
  public SessionAccessDeniedException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
