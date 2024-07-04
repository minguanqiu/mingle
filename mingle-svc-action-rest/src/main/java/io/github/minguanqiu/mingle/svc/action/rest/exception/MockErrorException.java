package io.github.minguanqiu.mingle.svc.action.rest.exception;

/**
 * Exception for mock data parse error.
 *
 * @author Qiu Guan Ming
 */

public class MockErrorException extends RuntimeException {

  /**
   * Exception message constant.
   */
  public static final String MSG = "mock error";

  /**
   * Create a new MockErrorException instance.
   *
   * @param e the exception.
   */
  public MockErrorException(Exception e) {
    super(MSG, e);
  }

}
