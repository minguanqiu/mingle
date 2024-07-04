package io.github.minguanqiu.mingle.svc.action.exception.handler.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Model for exception.
 *
 * @author Qiu Guan Ming
 */

@Getter
@Setter
public class ActionExceptionModel {

  /**
   * Action response code.
   *
   * @param code the action response code.
   * @return return the action response code.
   */
  private String code;

  /**
   * Action response message.
   *
   * @param msg the action response message.
   * @return return the action response message.
   */
  private String msg;

}
