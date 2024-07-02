package io.github.minguanqiu.mingle.svc.action.rest;

import lombok.Getter;
import lombok.Setter;

/**
 * Response body common header model determine this action success or fail for rest action
 *
 * @author Qiu Guan Ming
 */

@Getter
@Setter
public class RestActionResponseBodyHeader {

  private String successCode;

  private String code;

  private String msg;

}
