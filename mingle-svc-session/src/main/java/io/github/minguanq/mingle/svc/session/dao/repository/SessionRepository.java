package io.github.minguanq.mingle.svc.session.dao.repository;

import io.github.minguanq.mingle.svc.redis.RedisKey;
import io.github.minguanq.mingle.svc.session.dao.entity.SessionEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Ming
 */
public interface SessionRepository extends CrudRepository<SessionEntity, RedisKey> {
}
