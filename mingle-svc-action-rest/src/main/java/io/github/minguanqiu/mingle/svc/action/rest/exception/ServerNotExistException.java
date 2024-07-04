package io.github.minguanqiu.mingle.svc.action.rest.exception;

/**
 * Exception server not exist.
 *
 * @author Qiu Guan Ming
 */
public class ServerNotExistException extends RuntimeException {

  /**
   * Create a new ServerNotExistException instance.
   *
   * @param msg the exception message.
   */
  public ServerNotExistException(String msg) {
    super(msg);
  }

}
