package io.github.minguanqiu.mingle.svc.action.rest.exception;

/**
 * Exception for action response body deserialize error
 *
 * @author Qiu Guan Ming
 */

public class ActionResponseBodyDeserializeErrorException extends RuntimeException {

  public static final String MSG = "resModel deserialize error";

  public ActionResponseBodyDeserializeErrorException() {
    super(MSG);
  }
}
