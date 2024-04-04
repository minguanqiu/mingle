package io.github.minguanq.mingle.svc.request;

import io.github.minguanq.mingle.svc.SvcRequest;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Ming
 */
@Setter
@Getter
public class SimpleSvcReq extends SvcRequest {

    private String text1;

    private String text2;

}
