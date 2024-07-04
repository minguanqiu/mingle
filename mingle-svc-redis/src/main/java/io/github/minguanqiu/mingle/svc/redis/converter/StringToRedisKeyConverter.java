package io.github.minguanqiu.mingle.svc.redis.converter;

import io.github.minguanqiu.mingle.svc.redis.RedisKey;
import java.util.List;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

/**
 * This class for spring redis repository key string to redisKey object converter.
 *
 * @author Qiu Guan Ming
 */
@ReadingConverter
public class StringToRedisKeyConverter implements Converter<String, RedisKey> {

  @Override
  public RedisKey convert(String source) {
    return new RedisKey(List.of(source.split(RedisKey.KEY_DELIMITER)));
  }

}
