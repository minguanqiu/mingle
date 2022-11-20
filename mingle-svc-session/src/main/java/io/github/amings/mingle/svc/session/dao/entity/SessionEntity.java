package io.github.amings.mingle.svc.session.dao.entity;

import io.github.amings.mingle.svc.redis.RedisEntity;
import io.github.amings.mingle.svc.redis.annotation.RedisPrefix;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * Session Entity
 *
 * @author Ming
 */

@Getter
@Setter
@RedisPrefix("session")
public class SessionEntity extends RedisEntity {

    private String id;

    private String type;

    private List<String> authorities;

    private Map<String, Object> sessionValue;

}
