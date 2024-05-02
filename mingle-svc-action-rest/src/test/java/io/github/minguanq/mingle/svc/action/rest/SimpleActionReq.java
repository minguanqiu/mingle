package io.github.minguanq.mingle.svc.action.rest;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Ming
 */
@Setter
@Getter
public class SimpleActionReq extends RestActionRequest {

    private String text1;

    private String text2;

}
