package io.github.minguanqiu.mingle.svc.session.exception;

/**
 * Exception for session token decryption error.
 *
 * @author Qiu Guan Ming
 */
public class SessionTokenDecryptionErrorException extends RuntimeException {

  /**
   * Exception message constant.
   */
  public static final String MSG = "Session token decryption error";

  /**
   * Create a new SessionTokenDecryptionErrorException instance.
   */
  public SessionTokenDecryptionErrorException() {
    super(MSG);
  }

}
