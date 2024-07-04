package io.github.minguanqiu.mingle.svc.session.dao;

import io.github.minguanqiu.mingle.svc.redis.RedisKeyTemplate;
import io.github.minguanqiu.mingle.svc.redis.RedisKeyTemplateDao;
import io.github.minguanqiu.mingle.svc.session.dao.entity.SvcSessionEntity;
import org.springframework.stereotype.Repository;

/**
 * DAO for session logic.
 *
 * @author Qiu Guan Ming
 */
@Repository
public class SvcSessionDao extends RedisKeyTemplateDao<SvcSessionEntity> {

  /**
   * Create a new SvcSessionDao instance.
   *
   * @param redisTemplate the redis template.
   */
  public SvcSessionDao(
      RedisKeyTemplate<SvcSessionEntity> redisTemplate) {
    super(redisTemplate);
  }

}
