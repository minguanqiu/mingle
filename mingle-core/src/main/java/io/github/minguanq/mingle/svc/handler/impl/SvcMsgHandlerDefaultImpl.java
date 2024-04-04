package io.github.minguanq.mingle.svc.handler.impl;

import io.github.minguanq.mingle.svc.handler.SvcMsgHandler;
import io.github.minguanq.mingle.svc.handler.SvcMsgListHandler;
import io.github.minguanq.mingle.svc.utils.StringUtils;
import jakarta.annotation.PostConstruct;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@inheritDoc}
 * Default impl for {@link SvcMsgHandler}
 *
 * @author Ming
 */

public class SvcMsgHandlerDefaultImpl implements SvcMsgHandler {

    private final List<SvcMsgListHandler> svcMsgListHandlers;
    private final Map<String, Map<String, String>> msgMap = new HashMap<>();

    public SvcMsgHandlerDefaultImpl(List<SvcMsgListHandler> svcMsgListHandlers) {
        this.svcMsgListHandlers = svcMsgListHandlers;
    }

    /**
     * Get msg desc
     */
    @Override
    public String getMsg(String type, String code) {
        if (msgMap.containsKey(type)) {
            if (msgMap.get(type).containsKey(code)) {
                return msgMap.get(type).get(code);
            }
        }
        return null;
    }

    /**
     * Get type msg desc
     */
    @Override
    public String getMsg(String type, String code, Map<String, String> convertMap) {
        String msg = getMsg(type, code);
        if (msg != null && convertMap != null) {
            return StringUtils.templateConvert(msg, convertMap, "{", "}");
        }
        return null;
    }

    private void buildMsg(List<SvcMsgListHandler> handlers) {
        handlers.forEach(handler -> handler.getMsgList()
                .forEach(node -> msgMap
                        .computeIfAbsent(node.getMsgType(), k -> new HashMap<>())
                        .put(node.getCode(), node.getMsg())));
    }

    @PostConstruct
    public void init() {
        buildMsg(svcMsgListHandlers);
    }

}
