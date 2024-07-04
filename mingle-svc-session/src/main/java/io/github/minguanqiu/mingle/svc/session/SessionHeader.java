package io.github.minguanqiu.mingle.svc.session;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.minguanqiu.mingle.svc.redis.RedisKey;
import io.github.minguanqiu.mingle.svc.redis.json.RedisKeyJsonDeserializer;
import io.github.minguanqiu.mingle.svc.redis.json.RedisKeyJsonSerializer;

/**
 * The session token header.
 *
 * @param redisKey   the redis key.
 * @param timeToLive session live time.
 * @author Qiu Guan Ming
 */
public record SessionHeader(
    @JsonSerialize(using = RedisKeyJsonSerializer.class)
    @JsonDeserialize(using = RedisKeyJsonDeserializer.class) RedisKey redisKey,
    String timeToLive) {

}
