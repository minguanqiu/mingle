package io.github.amings.mingle.svc.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.Getter;

/**
 * Exception for service request model valid fail
 *
 * @author Ming
 */
@Getter
public class SvcReqModelValidFailException extends RuntimeException {

    private final ConstraintViolationException constraintViolationException;

    public SvcReqModelValidFailException(String message,ConstraintViolationException constraintViolationException) {
        super(message);
        this.constraintViolationException = constraintViolationException;
    }

}
