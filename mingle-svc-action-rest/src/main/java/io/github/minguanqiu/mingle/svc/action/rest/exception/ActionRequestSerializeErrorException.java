package io.github.minguanqiu.mingle.svc.action.rest.exception;

/**
 * Exception for action request serialize error.
 *
 * @author Qiu Guan Ming
 */
public class ActionRequestSerializeErrorException extends RuntimeException {

  /**
   * Exception message constant.
   */
  public static final String MSG = "request model serialize error";

  /**
   * Create a new ActionRequestSerializeErrorException instance.
   */
  public ActionRequestSerializeErrorException() {
    super(MSG);
  }

}
