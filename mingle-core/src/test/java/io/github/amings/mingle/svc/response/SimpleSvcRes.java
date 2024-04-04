package io.github.amings.mingle.svc.response;

import io.github.amings.mingle.svc.SvcResponse;
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
