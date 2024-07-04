package io.github.minguanqiu.mingle.svc.action.rest.exception;

/**
 * Exception for client error.
 *
 * @author Qiu Guan Ming
 */

public class ClientErrorException extends RuntimeException {

  /**
   * Create a new ClientErrorException instance.
   *
   * @param message the exception message.
   * @param cause   the cause exception.
   */
  public ClientErrorException(String message, Throwable cause) {
    super(message, cause);
  }

}
