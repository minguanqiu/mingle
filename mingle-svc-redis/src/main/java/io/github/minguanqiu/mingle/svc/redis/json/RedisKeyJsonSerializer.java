package io.github.minguanqiu.mingle.svc.redis.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import io.github.minguanqiu.mingle.svc.redis.RedisKey;
import java.io.IOException;

/**
 * This class for serialize redisKey object to json.
 *
 * @author Qiu Guan Ming
 */
public class RedisKeyJsonSerializer extends JsonSerializer<RedisKey> {

  @Override
  public void serialize(RedisKey redisKey, JsonGenerator jsonGenerator,
      SerializerProvider serializerProvider) throws IOException {
    jsonGenerator.writeString(redisKey.toString());
  }

  @Override
  public void serializeWithType(RedisKey value, JsonGenerator gen, SerializerProvider serializers,
      TypeSerializer typeSer) throws IOException {
    serialize(value, gen, serializers);
  }
}
