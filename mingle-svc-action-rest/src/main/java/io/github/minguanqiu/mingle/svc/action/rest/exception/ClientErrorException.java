package io.github.minguanqiu.mingle.svc.action.rest.exception;

/**
 * Exception for client error
 *
 * @author Qiu Guan Ming
 */

public class ClientErrorException extends RuntimeException {

  public ClientErrorException(String message, Throwable cause) {
    super(message, cause);
  }

}
