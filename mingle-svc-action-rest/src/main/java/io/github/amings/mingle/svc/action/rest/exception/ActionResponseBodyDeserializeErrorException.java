package io.github.amings.mingle.svc.action.rest.exception;

/**
 * Exception for action response body deserialize error
 *
 * @author Ming
 */

public class ActionResponseBodyDeserializeErrorException extends RuntimeException {

    public ActionResponseBodyDeserializeErrorException(String message) {
        super(message);
    }
}
