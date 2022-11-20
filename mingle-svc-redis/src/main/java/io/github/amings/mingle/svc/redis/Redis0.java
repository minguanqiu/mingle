package io.github.amings.mingle.svc.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Default database 0
 *
 * @author Ming
 */

@Service
public class Redis0<V extends RedisEntity> extends RedisForValue<V> {

    @Autowired
    public Redis0(@Qualifier("svcRedisTemplate") RedisTemplate<String, Object> redisTemplate) {
        super(redisTemplate);
    }

}
