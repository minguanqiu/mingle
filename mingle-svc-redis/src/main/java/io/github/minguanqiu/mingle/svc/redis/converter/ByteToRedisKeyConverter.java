package io.github.minguanqiu.mingle.svc.redis.converter;

import io.github.minguanqiu.mingle.svc.redis.RedisKey;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.util.List;

/**
 * @author Ming
 */
@ReadingConverter
public class ByteToRedisKeyConverter implements Converter<byte[], RedisKey> {

    @Override
    public RedisKey convert(byte[] source) {
        String key = new String(source);
        return new RedisKey(List.of(key.split(RedisKey.KEY_DELIMITER)));
    }

}
