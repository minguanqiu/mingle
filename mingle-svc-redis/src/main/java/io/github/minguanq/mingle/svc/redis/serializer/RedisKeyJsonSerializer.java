package io.github.minguanq.mingle.svc.redis.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.github.minguanq.mingle.svc.redis.RedisKey;

import java.io.IOException;

/**
 * @author Ming
 */
public class RedisKeyJsonSerializer extends JsonSerializer<RedisKey> {
    @Override
    public void serialize(RedisKey redisKey, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(redisKey.toString());
    }
}
