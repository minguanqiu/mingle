package io.github.minguanqiu.mingle.svc.exception;

/**
 * Exception for ip address authentication fail
 *
 * @author Qiu Guan Ming
 */
public class IPAuthenticationFailException extends RuntimeException {

  /**
   * Exception message constant.
   */
  public static final String MSG = "IP authentication fail";

  /**
   * Create a new IPAuthenticationFailException instance.
   */
  public IPAuthenticationFailException() {
    super(MSG);
  }

}
