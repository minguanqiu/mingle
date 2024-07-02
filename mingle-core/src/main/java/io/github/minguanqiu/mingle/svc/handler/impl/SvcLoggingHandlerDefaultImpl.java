package io.github.minguanqiu.mingle.svc.handler.impl;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.minguanqiu.mingle.svc.filter.SvcInfo;
import io.github.minguanqiu.mingle.svc.handler.SvcLoggingHandler;
import io.github.minguanqiu.mingle.svc.utils.DateUtils;
import io.github.minguanqiu.mingle.svc.utils.JacksonUtils;
import java.time.temporal.ChronoUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * {@inheritDoc} Default impl for {@link SvcLoggingHandler}
 *
 * @author Qiu Guan Ming
 */
@Slf4j
public class SvcLoggingHandlerDefaultImpl implements SvcLoggingHandler {

  private final JacksonUtils jacksonUtils;

  public SvcLoggingHandlerDefaultImpl(JacksonUtils jacksonUtils) {
    this.jacksonUtils = jacksonUtils;
  }

  @Override
  public void writeBeginLog(SvcInfo svcInfo) {
    ObjectNode objectNode = jacksonUtils.getObjectNode();
    objectNode.put("svcSerialNum", svcInfo.getSvcSerialNum());
    objectNode.put("name", svcInfo.getSvcDefinition().getSvcName());
    objectNode.put("startDateTime",
        DateUtils.dateTimeFormat(svcInfo.getStartDateTime(), "yyyy/MM/dd HH:mm:ss").get());
    jacksonUtils.readTree(svcInfo.getRequestBody()).ifPresent(data -> {
      objectNode.set("request", data);
    });
    log.info("【Svc Request】: {}", objectNode);
  }

  @Override
  public void writeEndLog(SvcInfo svcInfo) {
    ObjectNode objectNode = jacksonUtils.getObjectNode();
    objectNode.put("svcSerialNum", svcInfo.getSvcSerialNum());
    objectNode.put("endDateTime",
        DateUtils.dateTimeFormat(svcInfo.getEndDateTime(), "yyyy/MM/dd HH:mm:ss").get());
    objectNode.set("response", svcInfo.getResponseBody());
    objectNode.put("runTime",
        DateUtils.between(ChronoUnit.MILLIS, svcInfo.getStartDateTime(), svcInfo.getEndDateTime())
            + " ms");
    log.info("【Svc Response】: {}", objectNode);
  }

}
