package io.github.minguanqiu.mingle.svc.handler.impl;

import io.github.minguanqiu.mingle.svc.SvcTestUtils;
import io.github.minguanqiu.mingle.svc.configuration.properties.SvcProperties;
import io.github.minguanqiu.mingle.svc.handler.CodeMessageListHandler;
import io.github.minguanqiu.mingle.svc.handler.model.CodeMessage;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * @author Qiu Guan Ming
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
    codeMessages.add(
        svcMsgModel(svcProperties.getMsgType(), SvcTestUtils.X001, "convertX001 {var}"));
    codeMessages.add(
        svcMsgModel(svcProperties.getMsgType(), SvcTestUtils.X002, "convertX002 {var}"));
    return codeMessages;
  }

  public CodeMessage svcMsgModel(String msgType, String code, String msg) {
    return new CodeMessage(msgType, code, msg);
  }

}
