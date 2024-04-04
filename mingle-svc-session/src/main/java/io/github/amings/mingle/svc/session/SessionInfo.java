package io.github.amings.mingle.svc.session;

import io.github.amings.mingle.svc.redis.RedisKey;
import io.github.amings.mingle.svc.session.dao.entity.Session;

/**
 * @author Ming
 */
public record SessionInfo(SessionHeader sessionHeader, Session session) {

    public record SessionHeader(RedisKey redisKey) {
    }

}
