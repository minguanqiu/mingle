package io.github.minguanqiu.mingle.svc.redis;

import org.springframework.data.repository.CrudRepository;

/**
 * @author Qiu Guan Ming
 */
public interface SimpleRepository extends CrudRepository<SimpleEntity, RedisKey> {

}
