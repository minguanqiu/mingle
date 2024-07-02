package io.github.minguanqiu.mingle.svc.redis.serializer;

import io.github.minguanqiu.mingle.svc.redis.RedisKey;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * Customize spring redis key serializer for {@link RedisKey}
 *
 * @author Qiu Guan Ming
 */
public class RedisKeySerializer implements RedisSerializer<RedisKey> {

  @Override
  public byte[] serialize(RedisKey redisKey) throws SerializationException {
    assert redisKey != null;
    return redisKey.toString().getBytes(StandardCharsets.UTF_8);
  }

  @Override
  public RedisKey deserialize(byte[] bytes) throws SerializationException {
    String key = new String(bytes);
    return new RedisKey(List.of(key.split(RedisKey.KEY_DELIMITER)));
  }

}
