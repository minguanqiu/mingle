package io.github.minguanqiu.mingle.svc.exception;

/**
 * Exception for ip address authentication fail
 *
 * @author Qiu Guan Ming
 */
public class IPAuthenticationFailException extends RuntimeException {

  public static final String MSG = "IP authentication fail";

  public IPAuthenticationFailException() {
    super(MSG);
  }

}
