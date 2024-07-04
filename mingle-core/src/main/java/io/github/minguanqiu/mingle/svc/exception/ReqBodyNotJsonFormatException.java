package io.github.minguanqiu.mingle.svc.exception;

/**
 * Exception for request body not a json format
 *
 * @author Qiu Guan Ming
 */
public class ReqBodyNotJsonFormatException extends RuntimeException {

  /**
   * Exception message constant.
   */
  public static final String MSG = "Request model deserialize fail";

  /**
   * Create a new ReqBodyNotJsonFormatException instance.
   */
  public ReqBodyNotJsonFormatException() {
    super(MSG);
  }

}
