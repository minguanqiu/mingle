package io.github.minguanqiu.mingle.svc.exception;

/**
 * Exception for service not found
 *
 * @author Qiu Guan Ming
 */
public class SvcNotFoundException extends RuntimeException {

  public static final String MSG = "Service not found";

  public SvcNotFoundException() {
    super(MSG);
  }

}
