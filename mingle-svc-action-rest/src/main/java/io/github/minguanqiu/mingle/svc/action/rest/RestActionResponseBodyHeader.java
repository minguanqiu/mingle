package io.github.minguanqiu.mingle.svc.action.rest;

import lombok.Getter;
import lombok.Setter;

/**
 * Target API response body unite header determine this action success or fail for rest.
 *
 * @author Qiu Guan Ming
 */

@Getter
@Setter
public class RestActionResponseBodyHeader {

  /**
   * Target API success code.
   *
   * @param successCode the success code from target API.
   * @return return the success code from target API.
   */
  private String successCode;

  /**
   * Target API response code.
   *
   * @param code the response code from target API.
   * @return return the response code from target API.
   */
  private String code;

  /**
   * Target API response message.
   *
   * @param msg the response message from target API.
   * @return return the response message from target API.
   */
  private String msg;

}
