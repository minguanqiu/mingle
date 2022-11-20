package io.github.amings.mingle.svc.exception;

import lombok.Getter;

import javax.validation.ConstraintViolationException;

/**
 * @author Ming
 */
public class SvcReqModelValidFailException extends RuntimeException {

    @Getter
    private final ConstraintViolationException constraintViolationException;

    public SvcReqModelValidFailException(ConstraintViolationException constraintViolationException) {
        this.constraintViolationException = constraintViolationException;
    }

}
