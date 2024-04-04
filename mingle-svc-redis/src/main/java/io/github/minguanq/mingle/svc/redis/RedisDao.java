package io.github.minguanq.mingle.svc.redis;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * Base class for all redis dao
 *
 * @author Ming
 */
public class RedisDao<K, V> {

    protected RedisTemplate<K, V> redisTemplate;

    public RedisDao(RedisTemplate<K, V> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
