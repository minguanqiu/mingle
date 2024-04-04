package io.github.amings.mingle.svc.redis.serializer;

import io.github.amings.mingle.svc.redis.RedisKey;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Customize spring redis key serializer for {@link RedisKey}
 *
 * @author Ming
 */
public class RedisKeySerializer implements RedisSerializer<RedisKey> {

    @Override
    public byte[] serialize(RedisKey redisKey) throws SerializationException {
        return redisKey.toString().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public RedisKey deserialize(byte[] bytes) throws SerializationException {
        String key = new String(bytes);
        return new RedisKey(List.of(key.split(RedisKey.KEY_DELIMITER)));
    }

}
