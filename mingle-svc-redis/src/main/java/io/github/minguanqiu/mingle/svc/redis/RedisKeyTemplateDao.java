package io.github.minguanqiu.mingle.svc.redis;

/**
 * {@inheritDoc}
 * RedisDao impl for {@link RedisKeyTemplate}
 *
 * @author Ming
 */
public class RedisKeyTemplateDao<E> extends RedisTemplateDao<RedisKey, E> {

    public RedisKeyTemplateDao(RedisKeyTemplate<E> redisTemplate) {
        super(redisTemplate);
    }

}
