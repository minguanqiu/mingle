package io.github.minguanq.mingle.svc.action.rest.exception;

/**
 * Exception for action response body deserialize error
 *
 * @author Ming
 */

public class ActionResponseBodyDeserializeErrorException extends RuntimeException {

    public static final String MSG = "resModel deserialize error";

    public ActionResponseBodyDeserializeErrorException() {
        super(MSG);
    }
}
