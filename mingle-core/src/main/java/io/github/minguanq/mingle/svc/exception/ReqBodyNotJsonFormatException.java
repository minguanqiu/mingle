package io.github.minguanq.mingle.svc.exception;

/**
 * Exception for request body not a json format
 *
 * @author Ming
 */
public class ReqBodyNotJsonFormatException extends RuntimeException {

    public static final String MSG = "Request model deserialize fail";

    public ReqBodyNotJsonFormatException() {
        super(MSG);
    }

}
