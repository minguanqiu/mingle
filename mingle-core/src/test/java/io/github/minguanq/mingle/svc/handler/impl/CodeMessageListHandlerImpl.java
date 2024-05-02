package io.github.minguanq.mingle.svc.handler.impl;

import io.github.minguanq.mingle.svc.SvcTestUtils;
import io.github.minguanq.mingle.svc.configuration.properties.SvcProperties;
import io.github.minguanq.mingle.svc.handler.CodeMessageListHandler;
import io.github.minguanq.mingle.svc.handler.model.CodeMessage;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ming
 */
@Component
@Profile("test-code-message")
public class CodeMessageListHandlerImpl implements CodeMessageListHandler {

    private final SvcProperties svcProperties;

    public CodeMessageListHandlerImpl(SvcProperties svcProperties) {
        this.svcProperties = svcProperties;
    }

    @Override
    public List<CodeMessage> getMsgList() {
        ArrayList<CodeMessage> codeMessages = new ArrayList<>();
        codeMessages.add(svcMsgModel(svcProperties.getMsg_type(), SvcTestUtils.X001, "convertX001 {var}"));
        codeMessages.add(svcMsgModel(svcProperties.getMsg_type(), SvcTestUtils.X002, "convertX002 {var}"));
        return codeMessages;
    }

    public CodeMessage svcMsgModel(String msgType, String code, String msg) {
        return new CodeMessage(msgType, code, msg);
    }

}
