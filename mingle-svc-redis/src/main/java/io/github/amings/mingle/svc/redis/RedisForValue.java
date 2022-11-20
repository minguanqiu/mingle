package io.github.amings.mingle.svc.redis;

import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.Optional;
import java.util.Set;

/**
 * Default redisTemplate for String,Object
 *
 * @author Ming
 */

public abstract class RedisForValue<V extends RedisEntity> extends Redis {

    protected RedisTemplate<String, Object> redisTemplate;

    public RedisForValue(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void set(RedisKey redisKey, V value, Duration timeToLive) {
        redisTemplate.opsForValue().set(redisKey.format(), value, timeToLive);
    }

    @SuppressWarnings("unchecked")
    public Optional<V> get(RedisKey redisKey) {
        return Optional.ofNullable((V) redisTemplate.opsForValue().get(redisKey.format()));
    }

    public void getAndSet(RedisKey redisKey, V value) {
        redisTemplate.opsForValue().getAndSet(redisKey.format(), value);
    }

    public boolean hasKey(RedisKey redisKey) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(redisKey.format()));
    }

    public void del(RedisKey redisKey) {
        redisTemplate.delete(redisKey.format());
    }

    public void expire(RedisKey redisKey, Duration timeToLive) {
        redisTemplate.expire(redisKey.format(), timeToLive);
    }

    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

}
