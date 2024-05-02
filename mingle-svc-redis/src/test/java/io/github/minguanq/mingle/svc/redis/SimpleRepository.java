package io.github.minguanq.mingle.svc.redis;

import org.springframework.data.repository.CrudRepository;

/**
 * @author Ming
 */
public interface SimpleRepository extends CrudRepository<SimpleEntity,RedisKey> {
}
