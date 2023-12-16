package io.github.amings.mingle.svc.handler.impl;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.amings.mingle.svc.handler.ActionLogHandler;
import io.github.amings.mingle.svc.handler.model.ActionBeginModel;
import io.github.amings.mingle.svc.handler.model.ActionEndModel;
import io.github.amings.mingle.utils.DateUtils;
import io.github.amings.mingle.utils.JacksonUtils;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * {@link ActionLogHandler} impl
 *
 * @author Ming
 */

@Slf4j
public class ActionLogHandlerDefaultImpl implements ActionLogHandler {

    private final JacksonUtils jacksonUtils;

    public ActionLogHandlerDefaultImpl(JacksonUtils jacksonUtils) {
        this.jacksonUtils = jacksonUtils;
    }

    @Override
    public void writeBeginLog(ActionBeginModel model) {
        ObjectNode objectNode = jacksonUtils.getObjectNode();
        objectNode.put("svcUuid" ,model.getSvcUuid());
        objectNode.put("uuid" ,model.getUuid());
        objectNode.put("name" ,model.getName());
        objectNode.put("startDateTime" , DateUtils.AdDateTimeFormat(model.getStartDateTime(),"yyyy/MM/dd HH:mm:ss"));
        objectNode.put("reqBody","null");
        jacksonUtils.readTree(model.getRequestBody()).ifPresent(data -> {
            objectNode.set("reqBody" , data);
        });
        objectNode.put("type" , model.getType());
        log.info("【Action Request】: " + objectNode);
    }

    @Override
    public void writeEndLog(ActionEndModel model) {
        ObjectNode objectNode = jacksonUtils.getObjectNode();
        objectNode.put("svcUuid" ,model.getSvcUuid());
        objectNode.put("uuid" ,model.getUuid());
        objectNode.put("startDateTime" , DateUtils.AdDateTimeFormat(model.getEndDateTime(),"yyyy/MM/dd HH:mm:ss"));
        objectNode.put("resBody","null");
        jacksonUtils.readTree(model.getResponseBody()).ifPresent(data -> {
            objectNode.set("resBody" , data);
        });
        objectNode.put("code" ,model.getCode());
        objectNode.put("desc" ,model.getDesc());
        objectNode.put("runTime" ,model.getRunTime() + " ms");
        log.info("【Action Response】: " + objectNode);
    }

    @Override
    public void afterThrowing(Throwable t, String uuid, LocalDateTime endDateTime, String runTime) {
        log.error("【Action Exception】 : " + t.getMessage());
    }
}
