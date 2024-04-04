package io.github.minguanq.mingle.svc.session.dao;

import io.github.minguanq.mingle.svc.redis.AbstractRedisDao;
import io.github.minguanq.mingle.svc.redis.RedisKey;
import io.github.minguanq.mingle.svc.redis.RedisKeyTemplate;
import io.github.minguanq.mingle.svc.session.dao.entity.Session;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

/**
 * Dao for session logic
 *
 * @author Ming
 */

@Service
public class SessionDao extends AbstractRedisDao<Session> {

    public SessionDao(RedisKeyTemplate<Session> redisTemplate) {
        super(redisTemplate);
    }

    public Optional<Session> get(RedisKey redisKey) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(redisKey));
    }

    public void set(RedisKey redisKey, Session value, Duration timeToLive) {
        redisTemplate.opsForValue().set(redisKey, value, timeToLive);
    }

    public void del(RedisKey redisKey) {
        redisTemplate.delete((redisKey));
    }

}
