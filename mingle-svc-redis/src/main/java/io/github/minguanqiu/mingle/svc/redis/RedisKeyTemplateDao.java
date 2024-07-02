package io.github.minguanqiu.mingle.svc.redis;

import java.time.Duration;

/**
 * @author Qiu Gaun Ming
 */
public class RedisKeyTemplateDao<E extends RedisEntity> extends RedisTemplateDao<RedisKey, E> {

  public RedisKeyTemplateDao(
      RedisKeyTemplate<E> redisTemplate) {
    super(redisTemplate);
  }

  public void set(E entity) {
    redisTemplate.opsForValue()
        .set(entity.getRedisKey(), entity, Duration.ofSeconds(entity.getTimeToLive()));
  }

  public E get(RedisKey redisKey) {
    return redisTemplate.opsForValue().get(redisKey);
  }

  public void delete(RedisKey redisKey) {
    redisTemplate.delete(redisKey);
  }

  public boolean hasKey(RedisKey redisKey) {
    return Boolean.TRUE.equals(redisTemplate.hasKey(redisKey));
  }

}
