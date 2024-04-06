package io.github.minguanq.mingle.svc.exception.handler.model;

import io.github.minguanq.mingle.svc.SvcResponseBody;
import lombok.Getter;
import lombok.Setter;

/**
 * Exception message response model
 *
 * @author Ming
 */
@Getter
@Setter
public class AllExceptionModel extends SvcResponseBody {

    private String exception;
    private String causeException;
    private String msg;

}
