package io.github.minguanqiu.mingle.svc.handler.impl;

import io.github.minguanqiu.mingle.svc.handler.CodeMessageHandler;
import io.github.minguanqiu.mingle.svc.handler.CodeMessageListHandler;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Default implement for {@link CodeMessageHandler}.
 *
 * @author Qiu Guan Ming
 */

public class CodeMessageHandlerDefaultImpl implements CodeMessageHandler {

  private final List<CodeMessageListHandler> codeMessageListHandlers;
  private final Map<String, Map<String, String>> msgMap = new HashMap<>();

  /**
   * Create a new CodeMessageHandlerDefaultImpl instance.
   *
   * @param codeMessageListHandlers the code message list handlers.
   */
  public CodeMessageHandlerDefaultImpl(List<CodeMessageListHandler> codeMessageListHandlers) {
    this.codeMessageListHandlers = codeMessageListHandlers;
    init();
  }

  @Override
  public Optional<String> getMsg(String type, String code) {
    if (msgMap.containsKey(type)) {
      if (msgMap.get(type).containsKey(code)) {
        return Optional.ofNullable(msgMap.get(type).get(code));
      }
    }
    return Optional.empty();
  }

  /**
   * Build message from message handlers.
   *
   * @param handlers the code message list handlers.
   */
  private void buildMsg(List<CodeMessageListHandler> handlers) {
    handlers.forEach(handler -> handler.getMsgList()
        .forEach(node -> msgMap
            .computeIfAbsent(node.msgType(), k -> new HashMap<>())
            .put(node.code(), node.msg())));
  }

  /**
   * Initialized when the object is created
   */
  public void init() {
    buildMsg(codeMessageListHandlers);
  }

}
