package io.github.amings.mingle.svc.action.rest.exception;

/**
 * Exception for media type parse error
 *
 * @author Ming
 */

public class MediaTypeParseErrorException extends RuntimeException {

    public MediaTypeParseErrorException(String message) {
        super(message);
    }
}
