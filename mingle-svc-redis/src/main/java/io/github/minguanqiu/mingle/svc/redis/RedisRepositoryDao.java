package io.github.minguanqiu.mingle.svc.redis;

import org.springframework.data.repository.Repository;

/**
 * @author Ming
 */
public class RedisRepositoryDao<R extends Repository<? extends RedisEntity, RedisKey>> extends RedisDao {

    protected final R repository;

    public RedisRepositoryDao(R repository) {
        this.repository = repository;
    }

}
