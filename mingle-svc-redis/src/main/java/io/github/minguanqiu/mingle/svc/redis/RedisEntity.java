package io.github.minguanqiu.mingle.svc.redis;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

/**
 * Base class for all redis entity.
 *
 * @author Qiu Guan Ming
 */
@Getter
@RedisHash("undefined")
public class RedisEntity {

  /**
   * Redis key.
   *
   * @return return the redis key.
   */
  @Id
  private RedisKey redisKey;

  /**
   * Session live time.
   *
   * @param timeToLive the session live time.
   * @return return the session live time.
   */
  @Setter
  @TimeToLive
  private long timeToLive;

  /**
   * Create a new RedisEntity instance.
   *
   * @param redisKey   the redis key.
   * @param timeToLive the session live time.
   */
  public RedisEntity(RedisKey redisKey, long timeToLive) {
    this.redisKey = redisKey;
    this.timeToLive = timeToLive;
  }

}
