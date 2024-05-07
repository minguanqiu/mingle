package io.github.minguanqiu.mingle.svc.redis.converter;

import io.github.minguanqiu.mingle.svc.redis.RedisKey;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

/**
 * @author Ming
 */
@WritingConverter
public class RedisKeyToStringConverter implements Converter<RedisKey, String> {

    @Override
    public String convert(RedisKey source) {
        return source.toString();
    }

}
