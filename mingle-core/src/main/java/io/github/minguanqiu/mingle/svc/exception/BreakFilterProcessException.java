package io.github.minguanqiu.mingle.svc.exception;

import java.io.Serial;
import lombok.Getter;

/**
 * Exception for break filter process logic
 *
 * @author Qiu Guan Ming
 */
@Getter
public class BreakFilterProcessException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = 1L;

  /**
   * Service response code.
   *
   * @return return the service response code.
   */
  private final String code;

  /**
   * Service response message.
   *
   * @return return the service response message.
   */
  private final String msg;

  /**
   * Create a new BreakFilterProcessException instance.
   *
   * @param code the response code.
   * @param msg  the response message.
   */
  public BreakFilterProcessException(String code, String msg) {
    this.code = code;
    this.msg = msg;
  }

}
