package io.github.minguanq.mingle.svc.action.rest;

import io.github.minguanq.mingle.svc.SvcRequest;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Ming
 */
@Setter
@Getter
public class SimpleSvcReq extends SvcRequest {

    @NotEmpty
    private String text1;

    @NotEmpty
    private String text2;

}
