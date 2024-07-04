package io.github.minguanqiu.mingle.svc.handler;

import java.util.Optional;

/**
 * Handler for service message.
 *
 * @author Qiu Guan Ming
 */

public interface CodeMessageHandler {

  /**
   * Get service response message.
   *
   * @param type the service response message type.
   * @param code the service response code.
   * @return return the optional string object.
   */
  Optional<String> getMsg(String type, String code);

}
