package io.github.minguanq.mingle.svc.exception.handler.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.minguanq.mingle.svc.SvcResponseBody;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Ming
 */
@Getter
@Setter
public class TestExceptionModel extends SvcResponseBody {

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public TestExceptionModel(@JsonProperty("exceptionMsg") String exceptionMsg) {
        this.exceptionMsg = exceptionMsg;
    }

    private String exceptionMsg;

}
