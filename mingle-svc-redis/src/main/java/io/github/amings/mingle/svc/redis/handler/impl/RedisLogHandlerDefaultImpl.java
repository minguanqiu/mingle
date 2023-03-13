package io.github.amings.mingle.svc.redis.handler.impl;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.amings.mingle.svc.redis.handler.RedisLogHandler;
import io.github.amings.mingle.svc.redis.handler.model.RedisLogBeginModel;
import io.github.amings.mingle.svc.redis.handler.model.RedisLogEndModel;
import io.github.amings.mingle.utils.DateUtils;
import io.github.amings.mingle.utils.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.LocalDateTime;

/**
 * {@link RedisLogHandler} impl
 *
 * @author Ming
 */

@Slf4j
public class RedisLogHandlerDefaultImpl implements RedisLogHandler {

    @Autowired
    @Qualifier("redisLogJacksonUtils")
    JacksonUtils jacksonUtils;

    @Override
    public void writeBeginLog(RedisLogBeginModel model) {
        ObjectNode objectNode = jacksonUtils.getObjectNode();
        objectNode.put("svcUuid" ,model.getSvcUuid());
        objectNode.put("uuid" ,model.getUuid());
        objectNode.put("name" ,model.getName());
        objectNode.put("startDateTime" , DateUtils.AdDateTimeFormat(model.getStartDateTime(),"yyyy/MM/dd HH:mm:ss"));
        objectNode.put("reqBody","null");
        jacksonUtils.readTree(model.getRequestBody()).ifPresent(data -> {
            objectNode.set("reqBody" , data);
        });
        log.info("【Redis Request】 : " + objectNode);
    }

    @Override
    public void writeEndLog(RedisLogEndModel model) {
        ObjectNode objectNode = jacksonUtils.getObjectNode();
        objectNode.put("svcUuid" ,model.getSvcUuid());
        objectNode.put("uuid" ,model.getUuid());
        objectNode.put("startDateTime" , DateUtils.AdDateTimeFormat(model.getEndDateTime(),"yyyy/MM/dd HH:mm:ss"));
        objectNode.put("resBody" , "null");
        jacksonUtils.readTree(model.getResponseBody()).ifPresent(data -> {
            objectNode.set("resBody" , data);
        });
        objectNode.put("runTime" ,model.getRunTime() + " ms");
        log.info("【Redis Response】 : " + objectNode);
    }

    @Override
    public void afterThrowing(Throwable t, String uuid, LocalDateTime endDateTime, String runTime) {
        log.error("【Redis Exception】 : " + t.getMessage());
    }
}
