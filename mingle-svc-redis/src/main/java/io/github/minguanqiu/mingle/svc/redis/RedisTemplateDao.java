package io.github.minguanqiu.mingle.svc.redis;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * Base class for all redis template DAO.
 *
 * @param <K> the redis key.
 * @param <V> the redis value.
 * @author Qiu Guan Ming
 */
public class RedisTemplateDao<K, V> extends RedisDao {

  /**
   * Spring redis template.
   */
  protected RedisTemplate<K, V> redisTemplate;

  /**
   * Create a new RedisTemplateDao instance.
   *
   * @param redisTemplate the spring redis template.
   */
  public RedisTemplateDao(RedisTemplate<K, V> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }
}
