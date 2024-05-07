package io.github.minguanqiu.mingle.svc.handler.impl;

import io.github.minguanqiu.mingle.svc.handler.CodeMessageHandler;
import io.github.minguanqiu.mingle.svc.handler.CodeMessageListHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * {@inheritDoc}
 * Default impl for {@link CodeMessageHandler}
 *
 * @author Ming
 */

public class CodeMessageHandlerDefaultImpl implements CodeMessageHandler {

    private final List<CodeMessageListHandler> codeMessageListHandlers;
    private final Map<String, Map<String, String>> msgMap = new HashMap<>();

    public CodeMessageHandlerDefaultImpl(List<CodeMessageListHandler> codeMessageListHandlers) {
        this.codeMessageListHandlers = codeMessageListHandlers;
        init();
    }

    /**
     * Get map mapping message
     */
    @Override
    public Optional<String> getMsg(String type, String code) {
        if (msgMap.containsKey(type)) {
            if (msgMap.get(type).containsKey(code)) {
                return Optional.ofNullable(msgMap.get(type).get(code));
            }
        }
        return Optional.empty();
    }

    private void buildMsg(List<CodeMessageListHandler> handlers) {
        handlers.forEach(handler -> handler.getMsgList()
                .forEach(node -> msgMap
                        .computeIfAbsent(node.msgType(), k -> new HashMap<>())
                        .put(node.code(), node.msg())));
    }

    public void init() {
        buildMsg(codeMessageListHandlers);
    }

}
