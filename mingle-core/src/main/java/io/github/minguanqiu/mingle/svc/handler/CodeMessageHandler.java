package io.github.minguanqiu.mingle.svc.handler;

import java.util.Optional;

/**
 * Handler for service message
 *
 * @author Ming
 */

public interface CodeMessageHandler {

    Optional<String> getMsg(String type, String code);

}
