package io.github.minguanqiu.mingle.svc.exception;

/**
 * Exception for service not found
 *
 * @author Qiu Guan Ming
 */
public class SvcNotFoundException extends RuntimeException {

  /**
   * Exception message constant.
   */
  public static final String MSG = "Service not found";

  /**
   * Create a new SvcNotFoundException instance.
   */
  public SvcNotFoundException() {
    super(MSG);
  }

}
