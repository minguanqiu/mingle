package io.github.amings.mingle.svc.session.dao.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.amings.mingle.svc.redis.RedisEntity;
import io.github.amings.mingle.svc.redis.annotation.RedisPrefix;
import lombok.Builder;
import lombok.Getter;

import java.time.Duration;
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
@Builder
@RedisPrefix("session")
public class Session extends RedisEntity {

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Session(@JsonProperty("type") String type, @JsonProperty("id") String id, @JsonProperty("authorities") List<String> authorities, @JsonProperty("sessionValue") Map<String, Object> sessionValue, @JsonProperty("timeToLive") Duration timeToLive) {
        this.type = type;
        this.id = id;
        this.authorities = authorities;
        this.sessionValue = sessionValue;
        this.timeToLive = timeToLive;
    }

    private String type;

    private String id;

    @Builder.Default
    private List<String> authorities = new ArrayList<>();
    @Builder.Default
    private Map<String, Object> sessionValue = new HashMap<>();
    @Builder.Default
    private Duration timeToLive = Duration.ofMinutes(10);

    public static SessionBuilder builder(String type) {
        return new SessionBuilder().type(type);
    }

}
