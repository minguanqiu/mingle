package io.github.minguanqiu.mingle.svc.session.exception;

/**
 * Exception for session header missing
 *
 * @author Ming
 */
public class SessionHeaderDeserializeErrorException extends RuntimeException {

    public static final String MSG = "session header deserialize error";

    public SessionHeaderDeserializeErrorException() {
        super(MSG);
    }
}
