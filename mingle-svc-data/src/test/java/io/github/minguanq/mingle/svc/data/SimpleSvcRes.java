package io.github.minguanq.mingle.svc.data;

import io.github.minguanq.mingle.svc.SvcResponseBody;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Ming
 */
@Setter
@Getter
public class SimpleSvcRes extends SvcResponseBody {

    private String text1;

    private String text2;

}
