package io.github.minguanq.mingle.svc.redis;

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

        public RedisKeyBuilder addParam(String param) {
            params.add(param);
            return this;
        }

        public RedisKey build() {
            Assert.notEmpty(params, "key params is empty");
            return new RedisKey(params);
        }

    }

}
