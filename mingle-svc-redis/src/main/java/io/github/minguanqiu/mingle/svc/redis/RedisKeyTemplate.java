package io.github.minguanqiu.mingle.svc.redis;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author Qiu Gaun Ming
 */
public class RedisKeyTemplate<E extends RedisEntity> extends RedisTemplate<RedisKey,E> {

}
