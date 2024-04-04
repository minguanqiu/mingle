package io.github.amings.mingle.svc.handler.impl;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.amings.mingle.svc.filter.SvcInfo;
import io.github.amings.mingle.svc.handler.SvcLogHandler;
import io.github.amings.mingle.svc.utils.DateUtils;
import io.github.amings.mingle.svc.utils.JacksonUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.temporal.ChronoUnit;

/**
 * {@inheritDoc}
 * Default impl for {@link SvcLogHandler}
 *
 * @author Ming
 */
@Slf4j
public class SvcLogHandlerDefaultImpl implements SvcLogHandler {

    private final JacksonUtils jacksonUtils;

    public SvcLogHandlerDefaultImpl(JacksonUtils jacksonUtils) {
        this.jacksonUtils = jacksonUtils;
    }

    @Override
    public void writeBeginLog(SvcInfo svcInfo) {
        ObjectNode objectNode = jacksonUtils.getObjectNode();
        objectNode.put("svcSerialNum", svcInfo.getSvcSerialNum());
        objectNode.put("name", svcInfo.getSvcBinderModel().getSvcName());
        objectNode.put("startDateTime", DateUtils.dateTimeFormat(svcInfo.getStartDateTime(), "yyyy/MM/dd HH:mm:ss").get());
        jacksonUtils.readTree(svcInfo.getPayLoadString()).ifPresent(data -> {
            objectNode.set("reqBody", data);
        });
        log.info("【Svc Request】: " + objectNode);
    }

    @Override
    public void writeEndLog(SvcInfo svcInfo) {
        ObjectNode objectNode = jacksonUtils.getObjectNode();
        objectNode.put("uuid", svcInfo.getSvcSerialNum());
        objectNode.put("endDateTime", DateUtils.dateTimeFormat(svcInfo.getEndDateTime(), "yyyy/MM/dd HH:mm:ss").get());
        objectNode.put("code", svcInfo.getSvcResponseHandler().getCode());
        objectNode.put("msg", svcInfo.getSvcResponseHandler().getMsg());
        objectNode.set("resBody", svcInfo.getSvcResponseHandler().getResponseBody());
        objectNode.put("runTime", DateUtils.between(ChronoUnit.MILLIS, svcInfo.getStartDateTime(), svcInfo.getEndDateTime()) + " ms");
        log.info("【Svc Response】: " + objectNode);
    }

}
