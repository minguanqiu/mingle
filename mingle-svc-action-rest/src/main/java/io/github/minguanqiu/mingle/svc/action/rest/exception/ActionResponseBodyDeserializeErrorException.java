package io.github.minguanqiu.mingle.svc.action.rest.exception;

/**
 * Exception for action response body deserialize error.
 *
 * @author Qiu Guan Ming
 */

public class ActionResponseBodyDeserializeErrorException extends RuntimeException {

  /**
   * Exception message constant.
   */
  public static final String MSG = "resModel deserialize error";

  /**
   * Create a new ActionResponseBodyDeserializeErrorException instance.
   */
  public ActionResponseBodyDeserializeErrorException() {
    super(MSG);
  }
}
