package io.github.minguanqiu.mingle.svc.session.dao;

import io.github.minguanqiu.mingle.svc.redis.RedisKeyTemplate;
import io.github.minguanqiu.mingle.svc.redis.RedisKeyTemplateDao;
import io.github.minguanqiu.mingle.svc.session.dao.entity.SvcSessionEntity;
import org.springframework.stereotype.Repository;

/**
 * Dao for session logic
 *
 * @author Qiu Guan Ming
 */
@Repository
public class SvcSessionDao extends RedisKeyTemplateDao<SvcSessionEntity> {

  public SvcSessionDao(
      RedisKeyTemplate<SvcSessionEntity> redisTemplate) {
    super(redisTemplate);
  }

}
