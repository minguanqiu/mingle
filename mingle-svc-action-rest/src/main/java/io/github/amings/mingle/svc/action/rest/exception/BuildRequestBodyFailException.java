package io.github.amings.mingle.svc.action.rest.exception;

/**
 * @author Ming
 */

public class BuildRequestBodyFailException extends RuntimeException {

    public BuildRequestBodyFailException(Exception e) {
        super(e);
    }

}
