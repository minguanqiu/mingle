package io.github.minguanqiu.mingle.svc.action.handler.impl;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.minguanqiu.mingle.svc.action.AbstractAction;
import io.github.minguanqiu.mingle.svc.action.ActionInfo;
import io.github.minguanqiu.mingle.svc.action.handler.ActionLoggingHandler;
import io.github.minguanqiu.mingle.svc.utils.DateUtils;
import io.github.minguanqiu.mingle.svc.utils.JacksonUtils;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * Default implement for {@link ActionLoggingHandler}.
 *
 * @author Qiu Gaun Ming
 */
@Slf4j
public class ActionLoggingDefaultHandlerImpl implements ActionLoggingHandler {

  private final JacksonUtils jacksonUtils;

  /**
   * Create a new ActionLoggingDefaultHandlerImpl instance.
   *
   * @param jacksonUtils the jackson utils.
   */
  public ActionLoggingDefaultHandlerImpl(JacksonUtils jacksonUtils) {
    this.jacksonUtils = jacksonUtils;
  }

  @Override
  public void writeBeginLog(AbstractAction action, ActionInfo actionInfo) {
    ObjectNode objectNode = jacksonUtils.getObjectNode();
    objectNode.put("svcSerialNum", actionInfo.getSvcSerialNum());
    objectNode.put("actSerialNum", actionInfo.getActSerialNum());
    objectNode.put("name", action.getClass().getSimpleName());
    objectNode.put("startDateTime",
        DateUtils.dateTimeFormat(actionInfo.getStartDateTime(), "yyyy/MM/dd HH:mm:ss").get());
    jacksonUtils.readTree(actionInfo.getActionRequest()).ifPresent(data -> {
      objectNode.set("request", data);
    });
    objectNode.put("type", action.getType());
    log.info("【Action Request】: {}", objectNode);
  }

  @Override
  public void writeEndLog(AbstractAction action, ActionInfo actionInfo) {
    LocalDateTime endDateTime = LocalDateTime.now();
    ObjectNode objectNode = jacksonUtils.getObjectNode();
    objectNode.put("svcSerialNum", actionInfo.getSvcSerialNum());
    objectNode.put("actSerialNum", actionInfo.getActSerialNum());
    objectNode.put("endDateTime",
        DateUtils.dateTimeFormat(endDateTime, "yyyy/MM/dd HH:mm:ss").get());
    jacksonUtils.readTree(actionInfo.getActionResponseBody()).ifPresent(data -> {
      objectNode.set("responseBody", data);
    });
    objectNode.put("runTime",
        DateUtils.between(ChronoUnit.MILLIS, actionInfo.getStartDateTime(), endDateTime)
            + " ms");
    log.info("【Action Response】: {}", objectNode);
  }

}
