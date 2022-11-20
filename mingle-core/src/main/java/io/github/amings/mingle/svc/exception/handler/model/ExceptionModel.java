package io.github.amings.mingle.svc.exception.handler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.amings.mingle.svc.SvcResModel;
import lombok.Data;

/**
 * @author Ming
 */

@Data
public class ExceptionModel extends SvcResModel {

    @JsonProperty("exception")
    private String exception;

}
