package io.github.minguanqiu.mingle.svc.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.Getter;

/**
 * Exception for service request model valid fail
 *
 * @author Qiu Guan Ming
 */
@Getter
public class SvcRequestValidFailException extends RuntimeException {

  /**
   * ConstraintViolationException.
   *
   * @return return the ConstraintViolationException.
   */
  private final ConstraintViolationException constraintViolationException;

  /**
   * Exception message constant.
   */
  public final static String MSG = "Request valid fail";

  /**
   * Create a new SvcRequestValidFailException instance.
   *
   * @param constraintViolationException the validation exception.
   */
  public SvcRequestValidFailException(ConstraintViolationException constraintViolationException) {
    super(MSG);
    this.constraintViolationException = constraintViolationException;
  }

}
