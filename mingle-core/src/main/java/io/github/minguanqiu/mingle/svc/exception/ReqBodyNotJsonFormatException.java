package io.github.minguanqiu.mingle.svc.exception;

/**
 * Exception for request body not a json format
 *
 * @author Qiu Guan Ming
 */
public class ReqBodyNotJsonFormatException extends RuntimeException {

  public static final String MSG = "Request model deserialize fail";

  public ReqBodyNotJsonFormatException() {
    super(MSG);
  }

}
