package io.github.minguanqiu.mingle.svc.session.dao.entity;

import io.github.minguanqiu.mingle.svc.redis.RedisEntity;
import io.github.minguanqiu.mingle.svc.redis.RedisKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;


/**
 * Entity for session object
 *
 * @author Qiu Guan Ming
 */
@Getter
@Setter
public class SvcSessionEntity extends RedisEntity {

  private final String type;

  private final String id;

  private List<String> authorities = new ArrayList<>();

  private Map<String, Object> sessionValue = new HashMap<>();

  public SvcSessionEntity(RedisKey redisKey, long timeToLive, String type, String id) {
    super(redisKey, timeToLive);
    this.type = type;
    this.id = id;
  }

}
