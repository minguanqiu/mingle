package io.github.amings.mingle.svc.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.Getter;

/**
 * @author Ming
 */
public class SvcReqModelValidFailException extends RuntimeException {

    @Getter
    private final ConstraintViolationException constraintViolationException;

    public SvcReqModelValidFailException(String message,ConstraintViolationException constraintViolationException) {
        super(message);
        this.constraintViolationException = constraintViolationException;
    }

}
