package io.github.amings.mingle.svc.redis.handler.impl;

import io.github.amings.mingle.svc.redis.handler.RedisLogHandler;
import io.github.amings.mingle.svc.redis.handler.model.RedisLogBeginModel;
import io.github.amings.mingle.svc.redis.handler.model.RedisLogEndModel;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * {@link RedisLogHandler} impl
 *
 * @author Ming
 */

@Slf4j
public class RedisLogHandlerDefaultImpl implements RedisLogHandler {

    @Override
    public void writeBeginLog(RedisLogBeginModel model) {
        log.info("【Redis Request】 : " + model.getRequestBody());
    }

    @Override
    public void writeEndLog(RedisLogEndModel model) {
        log.info("【Redis Response】 : " + model.getResponseBody());
    }

    @Override
    public void afterThrowing(Throwable t, String uuid, LocalDateTime endDateTime, String runTime) {
        log.error("【Redis Exception】 : " + t.getMessage());
    }
}
