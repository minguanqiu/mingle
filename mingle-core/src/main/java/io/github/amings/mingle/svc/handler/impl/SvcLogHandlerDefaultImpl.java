package io.github.amings.mingle.svc.handler.impl;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.amings.mingle.svc.handler.SvcLogHandler;
import io.github.amings.mingle.svc.handler.model.SvcBeginModel;
import io.github.amings.mingle.svc.handler.model.SvcEndModel;
import io.github.amings.mingle.utils.DateUtils;
import io.github.amings.mingle.utils.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * {@link SvcLogHandler} impl
 *
 * @author Ming
 */

@Slf4j
public class SvcLogHandlerDefaultImpl implements SvcLogHandler {

    @Autowired
    @Qualifier("svcLogJacksonUtils")
    private JacksonUtils jacksonUtils;

    @Override
    public void writeBeginLog(SvcBeginModel model) {
        ObjectNode objectNode = jacksonUtils.getObjectNode();
        objectNode.put("uuid" ,model.getUuid());
        objectNode.put("name" ,model.getName());
        objectNode.put("startDateTime" , DateUtils.AdDateTimeFormat(model.getStartDateTime(),"yyyy/MM/dd HH:mm:ss"));
        objectNode.put("reqBody","null");
        jacksonUtils.readTree(model.getModelBody()).ifPresent(data -> {
            objectNode.set("reqBody" , data);
        });
        log.info("【Svc Request】: " + objectNode);
    }

    @Override
    public void writeEndLog(SvcEndModel model) {
        ObjectNode objectNode = jacksonUtils.getObjectNode();
        objectNode.put("uuid" ,model.getUuid());
        objectNode.put("startDateTime" , DateUtils.AdDateTimeFormat(model.getEndDateTime(),"yyyy/MM/dd HH:mm:ss"));
        objectNode.put("resBody","null");
        jacksonUtils.readTree(model.getResponseBody()).ifPresent(data -> {
            objectNode.set("resBody" , data);
        });
        objectNode.put("code" ,model.getCode());
        objectNode.put("desc" ,model.getDesc());
        objectNode.put("runTime" ,model.getRunTime() + " ms");
        log.info("【Svc Response】: " + objectNode);
    }

}
