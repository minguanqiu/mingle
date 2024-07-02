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

  private final String code;

  private final String msg;

  /**
   * @param code response code
   * @param msg  response msg
   */
  public BreakFilterProcessException(String code, String msg) {
    this.code = code;
    this.msg = msg;
  }

}
