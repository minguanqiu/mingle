package io.github.minguanqiu.mingle.svc.redis;

import org.springframework.stereotype.Repository;

/**
 * @author Qiu Gaun Ming
 */
@Repository
public class RedisKeyTemplateDaoImpl extends RedisKeyTemplateDao<SimpleEntity> {

  public RedisKeyTemplateDaoImpl(RedisKeyTemplate<SimpleEntity> redisTemplate) {
    super(redisTemplate);
  }

}
