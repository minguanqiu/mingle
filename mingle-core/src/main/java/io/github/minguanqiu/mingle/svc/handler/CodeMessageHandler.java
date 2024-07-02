package io.github.minguanqiu.mingle.svc.handler;

import java.util.Optional;

/**
 * Handler for service message
 *
 * @author Qiu Guan Ming
 */

public interface CodeMessageHandler {

  Optional<String> getMsg(String type, String code);

}
