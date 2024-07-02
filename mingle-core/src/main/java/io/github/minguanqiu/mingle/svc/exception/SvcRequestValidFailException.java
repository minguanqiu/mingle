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

  private final ConstraintViolationException constraintViolationException;

  public final static String MSG = "Request valid fail";

  public SvcRequestValidFailException(ConstraintViolationException constraintViolationException) {
    super(MSG);
    this.constraintViolationException = constraintViolationException;
  }

}
