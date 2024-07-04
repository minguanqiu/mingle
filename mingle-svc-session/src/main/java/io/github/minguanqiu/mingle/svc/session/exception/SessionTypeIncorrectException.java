package io.github.minguanqiu.mingle.svc.session.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Exception for session type incorrect.
 *
 * @author Qiu Guan Ming
 */
public class SessionTypeIncorrectException extends AuthenticationException {

  /**
   * Exception message constant.
   */
  public static final String MSG = "Session type incorrect";

  /**
   * Create a new SessionTypeIncorrectException instance.
   */
  public SessionTypeIncorrectException() {
    super(MSG);
  }

}
