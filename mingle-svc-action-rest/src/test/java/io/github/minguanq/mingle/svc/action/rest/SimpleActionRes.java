package io.github.minguanq.mingle.svc.action.rest;

import io.github.minguanq.mingle.svc.action.ActionResponseBody;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Ming
 */
@Getter
@Setter
public class SimpleActionRes extends ActionResponseBody {

    private String text1;

    private String text2;

}
