package io.github.minguanqiu.mingle.svc.exception;

/**
 * Exception for check mingle rules
 *
 * @author Qiu Guan Ming
 */

public class MingleRuntimeException extends RuntimeException {

  /**
   * Create a new IPAuthenticationFailException instance.
   *
   * @param msg exception message.
   */
  public MingleRuntimeException(String msg) {
    super(msg);
  }

}
