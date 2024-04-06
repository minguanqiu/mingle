package io.github.minguanq.mingle.svc.exception.handler.model;

import io.github.minguanq.mingle.svc.SvcResponseBody;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Ming
 */
@Getter
@Setter
public class TestExceptionModel extends SvcResponseBody {
    public TestExceptionModel(String exceptionMsg) {
        this.exceptionMsg = exceptionMsg;
    }

    private String exceptionMsg;

}
