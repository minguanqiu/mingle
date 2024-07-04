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
 * Entity for session.
 *
 * @author Qiu Guan Ming
 */
@Getter
@Setter
public class SvcSessionEntity extends RedisEntity {

  /**
   * Session type.
   *
   * @return return the session type.
   */
  private final String type;

  /**
   * Session unique value.
   *
   * @return Unique value.
   */
  private final String id;

  /**
   * List of service authorities.
   *
   * @param authorities the service authorities.
   * @return return the service authorities.
   */
  private List<String> authorities = new ArrayList<>();

  /**
   * Map of session storage cache value.
   *
   * @param sessionValue the session cache value.
   * @return return the session cache value.
   */
  private Map<String, Object> sessionValue = new HashMap<>();

  /**
   * Create a new SvcSessionEntity instance.
   *
   * @param redisKey   the redis key.
   * @param timeToLive the key live time.
   * @param type       the session type.
   * @param id         the session unique value.
   */
  public SvcSessionEntity(RedisKey redisKey, long timeToLive, String type, String id) {
    super(redisKey, timeToLive);
    this.type = type;
    this.id = id;
  }

}
