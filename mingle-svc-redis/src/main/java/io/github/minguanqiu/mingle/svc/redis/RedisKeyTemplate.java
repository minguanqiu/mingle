package io.github.minguanqiu.mingle.svc.redis;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * Spring redis template for redisKey type.
 *
 * @param <E> the redis entity.
 * @author Qiu Gaun Ming
 */
public class RedisKeyTemplate<E extends RedisEntity> extends RedisTemplate<RedisKey, E> {

}
