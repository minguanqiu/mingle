package io.github.amings.mingle.svc.action.rest.exception;

/**
 * Exception for action request serialize error
 *
 * @author Ming
 */
public class ActionRequestSerializeErrorException extends RuntimeException {

    public ActionRequestSerializeErrorException(String message) {
        super(message);
    }

}
