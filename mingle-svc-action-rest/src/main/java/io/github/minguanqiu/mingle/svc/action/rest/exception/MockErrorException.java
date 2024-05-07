package io.github.minguanqiu.mingle.svc.action.rest.exception;

/**
 * Exception for mock data parse error
 *
 * @author Ming
 */

public class MockErrorException extends RuntimeException {

    public static final String MSG = "mock error";

    public MockErrorException(Exception e) {
        super(MSG, e);
    }

}
