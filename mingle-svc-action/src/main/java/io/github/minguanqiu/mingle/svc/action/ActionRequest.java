package io.github.minguanqiu.mingle.svc.action;

import io.github.minguanqiu.mingle.svc.action.enums.AutoBreak;
import lombok.Getter;
import lombok.Setter;

/**
 * Base class for all action request.
 *
 * @author Qiu Guan Ming
 */
@Getter
@Setter
public class ActionRequest {

  /**
   * Automatic break action logic feature.
   *
   * @param autoBreak the automatic break action logic feature.
   * @return return the automatic break action logic value.
   */
  private AutoBreak autoBreak = AutoBreak.GLOBAL;

}
