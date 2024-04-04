package io.github.minguanq.mingle.svc.exception;

/**
 * Exception for request model deserialize fail
 *
 * @author Ming
 */
public class ReqModelDeserializeFailException extends RuntimeException {

    public ReqModelDeserializeFailException(String message) {
        super(message);
    }
}
