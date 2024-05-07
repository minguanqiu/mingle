package io.github.minguanqiu.mingle.svc.session;

import io.github.minguanqiu.mingle.svc.SvcRequest;
import io.github.minguanqiu.mingle.svc.annotation.ExcludeLog;
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
    private String action;

    @NotEmpty
    private String text1;

    @NotEmpty
    private String text2;

    @ExcludeLog
    private String text3;

}
