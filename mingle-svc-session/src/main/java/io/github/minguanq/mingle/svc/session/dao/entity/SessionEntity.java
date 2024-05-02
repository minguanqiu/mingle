package io.github.minguanq.mingle.svc.session.dao.entity;

import io.github.minguanq.mingle.svc.redis.RedisEntity;
import io.github.minguanq.mingle.svc.redis.RedisKey;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Entity for session object
 *
 * @author Ming
 */
@Getter
@Setter
public class SessionEntity extends RedisEntity {

    private final String type;

    private final String id;

    private List<String> authorities = new ArrayList<>();

    private Map<String, Object> sessionValue = new HashMap<>();

    public SessionEntity(RedisKey redisKey, long timeToLive, String type, String id) {
        super(redisKey, timeToLive);
        this.type = type;
        this.id = id;
    }
}
