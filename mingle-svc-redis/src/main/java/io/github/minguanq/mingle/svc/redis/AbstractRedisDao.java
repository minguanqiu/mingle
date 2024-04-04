package io.github.minguanq.mingle.svc.redis;

/**
 * {@inheritDoc}
 * RedisDao impl for {@link RedisKeyTemplate}
 *
 * @author Ming
 */

public abstract class AbstractRedisDao<E extends RedisEntity> extends RedisDao<RedisKey, E> {

    public AbstractRedisDao(RedisKeyTemplate<E> redisTemplate) {
        super(redisTemplate);
    }

}
