package io.github.amings.mingle.svc.redis;

import io.github.amings.mingle.svc.redis.annotation.RedisPrefix;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provide redis key generation and rule
 *
 * @author Ming
 */
public class RedisKey {

    public static final String KEY_DELIMITER = ":";

    private final List<String> key;

    public RedisKey(List<String> keyParams) {
        Assert.notNull(keyParams, "key params cannot be null");
        Assert.notEmpty(keyParams, "key params cannot be empty");
        this.key = keyParams;
    }

    public static RedisKeyBuilder builder() {
        return new RedisKeyBuilder();
    }

    @Override
    public String toString() {
        return String.join(KEY_DELIMITER, key);
    }

    public static class RedisKeyBuilder {
        private final List<String> params = new ArrayList<>();

        public RedisKeyBuilder addPrefix(Class<? extends RedisEntity> clazz) {
            RedisPrefix redisPrefix = clazz.getAnnotation(RedisPrefix.class);
            String prefix = "undefined";
            if (redisPrefix != null) {
                prefix = redisPrefix.value();
            }
            params.add(0, prefix);
            return this;
        }

        public RedisKeyBuilder addParam(String param) {
            params.add(param);
            return this;
        }

        public RedisKey build() {
            return new RedisKey(params);
        }

    }

}
