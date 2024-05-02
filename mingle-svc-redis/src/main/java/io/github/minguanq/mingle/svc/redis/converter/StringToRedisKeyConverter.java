package io.github.minguanq.mingle.svc.redis.converter;

import io.github.minguanq.mingle.svc.redis.RedisKey;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.util.List;

/**
 * @author Ming
 */
@ReadingConverter
public class StringToRedisKeyConverter implements Converter<String, RedisKey> {

    @Override
    public RedisKey convert(String source) {
        return new RedisKey(List.of(source.split(RedisKey.KEY_DELIMITER)));
    }

}
