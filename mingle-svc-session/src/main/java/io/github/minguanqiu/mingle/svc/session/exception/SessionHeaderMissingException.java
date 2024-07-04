package io.github.minguanqiu.mingle.svc.session.exception;

/**
 * Exception for session header missing.
 *
 * @author Qiu Guan Ming
 */
public class SessionHeaderMissingException extends RuntimeException {

  /**
   * Exception message constant.
   */
  public static final String MSG = "session header missing";

  /**
   * Create a new SessionHeaderMissingException instance.
   */
  public SessionHeaderMissingException() {
    super(MSG);
  }
}
