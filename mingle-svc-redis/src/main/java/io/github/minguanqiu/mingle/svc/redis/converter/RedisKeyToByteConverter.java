package io.github.minguanqiu.mingle.svc.redis.converter;

import io.github.minguanqiu.mingle.svc.redis.RedisKey;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import java.nio.charset.StandardCharsets;

/**
 * @author Ming
 */
@WritingConverter
public class RedisKeyToByteConverter implements Converter<RedisKey, byte[]> {

    @Override
    public byte[] convert(RedisKey source) {
        return source.toString().getBytes(StandardCharsets.UTF_8);
    }

}
