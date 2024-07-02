package io.github.minguanqiu.mingle.svc.session.exception;

/**
 * Exception for session token decryption error
 *
 * @author Qiu Guan Ming
 */
public class SessionTokenDecryptionErrorException extends RuntimeException {

  public static final String MSG = "Session token decryption error";

  public SessionTokenDecryptionErrorException() {
    super(MSG);
  }

}
