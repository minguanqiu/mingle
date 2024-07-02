package io.github.minguanqiu.mingle.svc.action.rest.exception;

/**
 * Exception for media type parse error
 *
 * @author Qiu Guan Ming
 */

public class MediaTypeParseErrorException extends RuntimeException {

  public static final String MSG = "mediaType parse error";

  public MediaTypeParseErrorException() {
    super(MSG);
  }
}
