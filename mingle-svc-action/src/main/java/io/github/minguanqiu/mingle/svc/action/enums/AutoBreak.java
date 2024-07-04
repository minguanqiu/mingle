package io.github.minguanqiu.mingle.svc.action.enums;

/**
 * Enum for action auto break feature.
 *
 * @author Qiu Guan Ming
 */
public enum AutoBreak {

  /**
   * Follow properties value.
   */
  GLOBAL,
  /**
   * The action fail will throw exception from action logic.
   */
  TRUE,
  /**
   * Even the action fails will not throw exception.
   */
  FALSE

}
