package io.github.minguanqiu.mingle.svc.session.exception;

/**
 * Exception for session token encryption error.
 *
 * @author Qiu Guan Ming
 */
public class SessionTokenEncryptionErrorException extends RuntimeException {

  /**
   * Exception message constant.
   */
  public static final String MSG = "Session token encryption error";

  /**
   * Create a new SessionTokenEncryptionErrorException instance.
   *
   * @param e the exception.
   */
  public SessionTokenEncryptionErrorException(Exception e) {
    super(MSG, e);
  }
}
