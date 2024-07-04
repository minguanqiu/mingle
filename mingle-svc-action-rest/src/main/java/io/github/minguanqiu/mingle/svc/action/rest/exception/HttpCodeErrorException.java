package io.github.minguanqiu.mingle.svc.action.rest.exception;

import io.github.minguanqiu.mingle.svc.action.rest.AbstractRestAction;
import lombok.Getter;

/**
 * Exception for http code error when not include {@link AbstractRestAction} successHttpCodeSet
 * range.
 *
 * @author Qiu Guan Ming
 */

@Getter
public class HttpCodeErrorException extends RuntimeException {

  /**
   * Http response code.
   *
   * @return return the http response code.
   */
  private final int code;

  /**
   * Create a new HttpCodeErrorException instance.
   *
   * @param code    the http response code.
   * @param message the exception message.
   */
  public HttpCodeErrorException(int code, String message) {
    super(message);
    this.code = code;
  }

}
