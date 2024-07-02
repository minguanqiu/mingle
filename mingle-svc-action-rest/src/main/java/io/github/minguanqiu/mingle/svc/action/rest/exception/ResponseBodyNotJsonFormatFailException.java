package io.github.minguanqiu.mingle.svc.action.rest.exception;

/**
 * Exception for response body not a json format
 *
 * @author Qiu Guan Ming
 */

public class ResponseBodyNotJsonFormatFailException extends RuntimeException {

  public static final String MSG = "response body not a json";

  public ResponseBodyNotJsonFormatFailException() {
    super(MSG);
  }
}
