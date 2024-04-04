package io.github.minguanq.mingle.svc.exception.handler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.minguanq.mingle.svc.SvcResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * Exception message response model
 *
 * @author Ming
 */
@Getter
@Setter
public class AllExceptionModel extends SvcResponse {

    @JsonProperty("exception")
    private String exception;
    @JsonProperty("causeException")
    private String causeException;
    @JsonProperty("msg")
    private String msg;

}
