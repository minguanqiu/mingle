package io.github.minguanqiu.mingle.svc.exception;

/**
 * Exception for request model deserialize fail
 *
 * @author Ming
 */
public class SvcRequestDeserializeFailException extends RuntimeException {

    public static final String MSG = "Request deserialize fail";

    public SvcRequestDeserializeFailException() {
        super(MSG);
    }

}
