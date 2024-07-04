package io.github.minguanqiu.mingle.svc.handler;

import io.github.minguanqiu.mingle.svc.handler.model.CodeMessage;
import java.util.List;

/**
 * Handler for service message,provide custom message list.
 *
 * @author Qiu Guan Ming
 */

public interface CodeMessageListHandler {

  /**
   * Build the service response message list.
   *
   * @return the list of code message.
   */
  List<CodeMessage> getMsgList();

}
