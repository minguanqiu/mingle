package io.github.minguanqiu.mingle.svc.exception;

/**
 * Exception for request model deserialize fail
 *
 * @author Qiu Guan Ming
 */
public class SvcRequestDeserializeFailException extends RuntimeException {

  /**
   * Exception message constant.
   */
  public static final String MSG = "Request deserialize fail";

  /**
   * Create a new SvcRequestDeserializeFailException instance.
   */
  public SvcRequestDeserializeFailException() {
    super(MSG);
  }

}
