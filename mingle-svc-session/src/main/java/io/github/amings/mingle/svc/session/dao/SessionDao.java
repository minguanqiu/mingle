package io.github.amings.mingle.svc.session.dao;

import io.github.amings.mingle.svc.redis.AbstractRedisDao;
import io.github.amings.mingle.svc.redis.Redis0;
import io.github.amings.mingle.svc.redis.RedisKey;
import io.github.amings.mingle.svc.session.dao.entity.SessionEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

/**
 * Session dao
 *
 * @author Ming
 */

@Service
public class SessionDao extends AbstractRedisDao<Redis0<SessionEntity>> {

    public Optional<SessionEntity> get(RedisKey redisKey) {
        return redis.get(redisKey);
    }

    public void set(RedisKey redisKey, SessionEntity value, Duration timeToLive) {
        redis.set(redisKey, value, timeToLive);
    }

    public void del(RedisKey redisKey) {
        redis.del(redisKey);
    }

}
