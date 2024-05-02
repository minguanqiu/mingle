package io.github.minguanq.mingle.svc.redis;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

/**
 * Base class for all redis entity
 *
 * @author Ming
 */
@Getter
@RedisHash("undefined")
public class RedisEntity {

    @Id
    private final RedisKey redisKey;

    @Setter
    @TimeToLive
    private long timeToLive;


    public RedisEntity(RedisKey redisKey, long timeToLive) {
        this.redisKey = redisKey;
        this.timeToLive = timeToLive;
    }

}
