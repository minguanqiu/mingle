package io.github.amings.mingle.svc.exception.handler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.amings.mingle.svc.SvcResModel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Ming
 */

@Getter
@Setter
public class ExceptionModel extends SvcResModel {

    @JsonProperty("exception")
    private String exception;
    @JsonProperty("causeException")
    private String causeException;
    @JsonProperty("msg")
    private String msg;

}
