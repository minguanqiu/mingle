package io.github.amings.mingle.svc.handler.impl;

import io.github.amings.mingle.svc.action.ActionResData;
import io.github.amings.mingle.svc.annotation.MingleSvcMsg;
import io.github.amings.mingle.svc.handler.SvcMsgHandler;
import io.github.amings.mingle.svc.handler.SvcMsgListHandler;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * {@link SvcMsgHandler} impl
 *
 * @author Ming
 */

public class SvcMsgHandlerDefaultImpl implements SvcMsgHandler {

    @Autowired
    List<SvcMsgListHandler> svcMsgListHandlers;
    private final Map<String, Map<String, String>> msgMap = new HashMap<>();

    /**
     * Get svc type msg desc
     */
    @Override
    public String getMsg(String code) {
        if (msgMap.containsKey("svc")) {
            if (msgMap.get("svc").containsKey(code)) {
                return msgMap.get("svc").get(code);
            }
        }
        return null;
    }

    /**
     * Get action type msg desc
     */
    @Override
    public String getMsg(ActionResData<?> actionResData) {
        if (msgMap.containsKey(actionResData.getMsgType())) {
            if (msgMap.get(actionResData.getMsgType()).containsKey(actionResData.getCode())) {
                return msgMap.get(actionResData.getMsgType()).get(actionResData.getCode());
            }
        }
        return actionResData.getDesc();
    }

    private void buildMsg(List<SvcMsgListHandler> handlers) {
        handlers.forEach(handler -> {
            handler.getMsgList().forEach(node -> {
                if (msgMap.containsKey(node.getMsgType())) {
                    msgMap.get(node.getMsgType()).put(node.getCode(), node.getDesc());
                } else {
                    Map<String, String> tmp = new HashMap<>();
                    tmp.put(node.getCode(), node.getDesc());
                    msgMap.put(node.getMsgType(), tmp);
                }
            });
        });
    }

    @PostConstruct
    private void init() {
        List<SvcMsgListHandler> mingleSvcMsgHandlers = svcMsgListHandlers.stream().filter(handler -> handler.getClass().getAnnotation(MingleSvcMsg.class) != null).collect(Collectors.toList());
        List<SvcMsgListHandler> svcMsgHandlers = svcMsgListHandlers.stream().filter(handler -> handler.getClass().getAnnotation(MingleSvcMsg.class) == null).collect(Collectors.toList());
        buildMsg(mingleSvcMsgHandlers);
        buildMsg(svcMsgHandlers);
    }

}
