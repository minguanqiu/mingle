package io.github.amings.mingle.svc.handler;

import io.github.amings.mingle.svc.action.ActionResData;

import java.util.Map;

/**
 * Svc response message handler
 *
 * @author Ming
 */

public interface SvcMsgHandler {

    String getMsg(String code);

    String getMsg(ActionResData<?> actionResData);

    String getMsg(String code, Map<String, String> values);

}
