package io.github.minguanqiu.mingle.svc.redis;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

/**
 * @author Qiu Guan Ming
 */
@Getter
@Setter
@RedisHash("simple")
public class SimpleEntity extends RedisEntity {

  public SimpleEntity(RedisKey redisKey, long timeToLive) {
    super(redisKey, timeToLive);
  }

  private String text1;

  private TestEntity testEntity = new TestEntity();

}
