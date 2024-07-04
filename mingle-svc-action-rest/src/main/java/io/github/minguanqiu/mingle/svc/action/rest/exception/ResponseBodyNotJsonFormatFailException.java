package io.github.minguanqiu.mingle.svc.action.rest.exception;

/**
 * Exception for response body not a json format.
 *
 * @author Qiu Guan Ming
 */

public class ResponseBodyNotJsonFormatFailException extends RuntimeException {

  /**
   * Exception message constant.
   */
  public static final String MSG = "response body not a json";

  /**
   * Create a new ResponseBodyNotJsonFormatFailException instance.
   */
  public ResponseBodyNotJsonFormatFailException() {
    super(MSG);
  }

}
