package io.github.minguanq.mingle.svc.response;

import io.github.minguanq.mingle.svc.SvcResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Ming
 */
@Setter
@Getter
public class SimpleSvcRes extends SvcResponse {

    private String text1;

    private String text2;

}
