package io.github.minguanqiu.mingle.svc.session;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.minguanqiu.mingle.svc.redis.RedisKey;
import io.github.minguanqiu.mingle.svc.redis.serializer.RedisKeyJsonDeserializer;
import io.github.minguanqiu.mingle.svc.redis.serializer.RedisKeyJsonSerializer;

/**
 * @author Ming
 */

public record SessionHeader(
        @JsonSerialize(using = RedisKeyJsonSerializer.class)
        @JsonDeserialize(using = RedisKeyJsonDeserializer.class) RedisKey redisKey,
        String refreshTime) {
}
