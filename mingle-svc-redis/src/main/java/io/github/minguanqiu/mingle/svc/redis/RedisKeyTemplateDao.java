package io.github.minguanqiu.mingle.svc.redis;

import java.time.Duration;

/**
 * Base class for all redis key template DAO.
 *
 * @param <E> the redis entity.
 * @author Qiu Gaun Ming
 */
public class RedisKeyTemplateDao<E extends RedisEntity> extends RedisTemplateDao<RedisKey, E> {

  /**
   * Create a new RedisKeyTemplateDao instance.
   *
   * @param redisTemplate the redis template.
   */
  public RedisKeyTemplateDao(
      RedisKeyTemplate<E> redisTemplate) {
    super(redisTemplate);
  }

  /**
   * Set redis entity.
   *
   * @param entity the redis entity.
   */
  public void set(E entity) {
    redisTemplate.opsForValue()
        .set(entity.getRedisKey(), entity, Duration.ofSeconds(entity.getTimeToLive()));
  }

  /**
   * Get the redis entity.
   *
   * @param redisKey the redis key
   * @return return the redis entity.
   */
  public E get(RedisKey redisKey) {
    return redisTemplate.opsForValue().get(redisKey);
  }

  /**
   * Del the redis entity.
   *
   * @param redisKey the redis key
   */
  public void delete(RedisKey redisKey) {
    redisTemplate.delete(redisKey);
  }

  /**
   * Check key exist.
   *
   * @param redisKey the redis key
   * @return return the ture of false.
   */
  public boolean hasKey(RedisKey redisKey) {
    return Boolean.TRUE.equals(redisTemplate.hasKey(redisKey));
  }

}
