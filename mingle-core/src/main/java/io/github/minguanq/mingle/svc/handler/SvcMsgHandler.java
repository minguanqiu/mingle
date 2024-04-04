package io.github.minguanq.mingle.svc.handler;

import java.util.Map;

/**
 * Handler for service message
 *
 * @author Ming
 */

public interface SvcMsgHandler {

    String getMsg(String type, String code);

    String getMsg(String type, String code, Map<String, String> convertMap);

}
