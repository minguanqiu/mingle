package io.github.minguanqiu.mingle.svc.action.rest;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Qiu Guan Ming
 */
@Setter
@Getter
public class SimpleActionReq extends RestActionRequest {

  private String text1;

  private String text2;

}
