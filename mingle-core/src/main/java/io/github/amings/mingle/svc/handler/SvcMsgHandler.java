package io.github.amings.mingle.svc.handler;

import java.util.Map;

/**
 * Svc response message handler
 *
 * @author Ming
 */

public interface SvcMsgHandler {

    String getMsg(String type, String code);

    String getMsg(String type, String code, Map<String, String> values);

}
