package io.github.minguanqiu.mingle.svc.redis.json;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import io.github.minguanqiu.mingle.svc.redis.RedisKey;
import java.io.IOException;
import java.util.Arrays;

/**
 * This class for json deserialize redisKey object.
 *
 * @author Qiu Guan Ming
 */
public class RedisKeyJsonDeserializer extends JsonDeserializer<RedisKey> {

  @Override
  public RedisKey deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
      throws IOException, JacksonException {
    return new RedisKey(Arrays.asList(jsonParser.getValueAsString().split(RedisKey.KEY_DELIMITER)));
  }

  @Override
  public Object deserializeWithType(JsonParser p, DeserializationContext ctxt,
      TypeDeserializer typeDeserializer) throws IOException, JacksonException {
    return deserialize(p, ctxt);
  }
}
