package io.github.minguanqiu.mingle.svc.redis;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * Base class for all redis dao
 *
 * @author Qiu Guan Ming
 */
public class RedisTemplateDao<K, V> extends RedisDao {

  protected RedisTemplate<K, V> redisTemplate;

  public RedisTemplateDao(RedisTemplate<K, V> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }
}
