package io.github.minguanq.mingle.svc.redis.serializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import io.github.minguanq.mingle.svc.redis.RedisKey;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author Ming
 */
public class RedisKeyJsonDeserializer extends JsonDeserializer<RedisKey> {
    @Override
    public RedisKey deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        return new RedisKey(Arrays.asList(jsonParser.getValueAsString().split(RedisKey.KEY_DELIMITER)));
    }
}
