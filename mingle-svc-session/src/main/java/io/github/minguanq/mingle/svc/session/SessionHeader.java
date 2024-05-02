package io.github.minguanq.mingle.svc.session;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.minguanq.mingle.svc.redis.RedisKey;
import io.github.minguanq.mingle.svc.redis.serializer.RedisKeyJsonDeserializer;
import io.github.minguanq.mingle.svc.redis.serializer.RedisKeyJsonSerializer;

/**
 * @author Ming
 */

public record SessionHeader(
        @JsonSerialize(using = RedisKeyJsonSerializer.class)
        @JsonDeserialize(using = RedisKeyJsonDeserializer.class) RedisKey redisKey,
        String refreshTime) {
}
