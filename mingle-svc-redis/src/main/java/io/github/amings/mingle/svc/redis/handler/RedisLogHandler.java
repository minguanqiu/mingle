package io.github.amings.mingle.svc.redis.handler;

import io.github.amings.mingle.svc.redis.handler.model.RedisLogBeginModel;
import io.github.amings.mingle.svc.redis.handler.model.RedisLogEndModel;

import java.time.LocalDateTime;

/**
 * Redis logging handler
 *
 * @author Ming
 */

public interface RedisLogHandler {

    void writeBeginLog(RedisLogBeginModel model);

    void writeEndLog(RedisLogEndModel model);

    void afterThrowing(Throwable t, String uuid, LocalDateTime endDateTime, String runTime);

}
