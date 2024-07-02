package io.github.minguanqiu.mingle.svc.action.rest.exception;

/**
 * Exception for action request serialize error
 *
 * @author Qiu Guan Ming
 */
public class ActionRequestSerializeErrorException extends RuntimeException {

  public static final String MSG = "request model serialize error";

  public ActionRequestSerializeErrorException() {
    super(MSG);
  }

}
