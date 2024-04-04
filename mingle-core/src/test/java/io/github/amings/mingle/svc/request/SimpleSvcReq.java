package io.github.amings.mingle.svc.request;

import io.github.amings.mingle.svc.SvcRequest;
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
