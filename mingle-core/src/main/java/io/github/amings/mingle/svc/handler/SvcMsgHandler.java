package io.github.amings.mingle.svc.handler;

import io.github.amings.mingle.svc.action.ActionResData;

/**
 * Svc response message handler
 *
 * @author Ming
 */

public interface SvcMsgHandler {

    String getMsg(String code);

    String getMsg(ActionResData<?> actionResData);

}
