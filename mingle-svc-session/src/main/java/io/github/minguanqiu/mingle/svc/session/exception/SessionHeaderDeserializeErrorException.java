package io.github.minguanqiu.mingle.svc.session.exception;

/**
 * Exception for session header missing.
 *
 * @author Qiu Guan Ming
 */
public class SessionHeaderDeserializeErrorException extends RuntimeException {

  /**
   * Exception message constant.
   */
  public static final String MSG = "session header deserialize error";

  /**
   * Create a new SessionHeaderDeserializeErrorException instance.
   */
  public SessionHeaderDeserializeErrorException() {
    super(MSG);
  }
}
