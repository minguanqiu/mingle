package io.github.minguanq.mingle.svc.action.enums;

/**
 * Enum for action auto break
 * GLOBAL - use properties value
 * TRUE - if the action fail will throw exception from action logic
 * FALSE - even the action fails will not throw exception
 *
 * @author Ming
 */
public enum AutoBreak {

    GLOBAL, TRUE, FALSE

}
