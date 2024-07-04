package io.github.minguanqiu.mingle.svc.session.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Exception for session not exist.
 *
 * @author Qiu Guan Ming
 */
public class SessionNotExistException extends AuthenticationException {

  /**
   * Exception message constant.
   */
  public static final String MSG = "Session not exist";

  /**
   * Create a new SessionNotExistException instance.
   */
  public SessionNotExistException() {
    super(MSG);
  }
}
