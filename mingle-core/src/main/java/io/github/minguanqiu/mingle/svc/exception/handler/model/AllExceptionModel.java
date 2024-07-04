package io.github.minguanqiu.mingle.svc.exception.handler.model;

import io.github.minguanqiu.mingle.svc.SvcResponseBody;
import lombok.Getter;
import lombok.Setter;

/**
 * Exception message response model.
 *
 * @author Qiu Guan Ming
 */
@Getter
@Setter
public class AllExceptionModel extends SvcResponseBody {

  /**
   * Exception name.
   *
   * @param exception the exception name.
   * @return return the exception name.
   */
  private String exception;
  /**
   * Cause exception name.
   *
   * @param causeException the cause exception name.
   * @return return the cause exception name.
   */
  private String causeException;
  /**
   * Exception message.
   *
   * @param msg the exception message.
   * @return return the exception message.
   */
  private String msg;

}
