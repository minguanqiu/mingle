package io.github.minguanq.mingle.svc.redis;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * {@link RedisTemplate} for redis key
 *
 * @author Ming
 */
public class RedisKeyTemplate<Entity extends RedisEntity> extends RedisTemplate<RedisKey, Entity> {

}
