package io.github.amings.mingle.svc.redis;

import io.github.amings.mingle.svc.redis.annotation.RedisPrefix;
import lombok.Getter;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Generate redis key prefix
 *
 * @author Ming
 */

public class RedisKey {

    private RedisKey(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    /**
     * Pass RedisEntity class to get prefix
     * @param clazz RedisEntity class
     */
    public RedisKey(Class<? extends RedisEntity> clazz) {
        Assert.notNull(clazz, "RedisEntity can not be null");
        RedisPrefix annotation = clazz.getAnnotation(RedisPrefix.class);
        Assert.notNull(annotation, "RedisPrefix can not be null");
        this.keyPrefix = annotation.value();
    }

    @Getter
    private final String keyPrefix;

    @Getter
    private final ArrayList<String> keyParams = new ArrayList<>();

    /**
     * @param params - Param for key suffix
     * @return RedisKey
     * Add key suffix
     */
    public RedisKey addParam(String[] params) {
        keyParams.addAll(Arrays.asList(params));
        return this;
    }

    /**
     * @param params - Param for key suffix
     * @return RedisKey
     * Add key suffix
     */
    public RedisKey addParams(List<String> params) {
        keyParams.addAll(params);
        return this;
    }

    /**
     * @param param - Param for key suffix
     * @return RedisKey
     * Add key suffix
     */
    public RedisKey addParam(String param) {
        keyParams.add(param);
        return this;
    }

    /**
     * @return String RedisKey string
     * Get key string
     */
    public String format() {
        ArrayList<String> keys = new ArrayList<>();
        keys.add(keyPrefix);
        keys.addAll(keyParams);
        return String.join(":", keys);
    }

    /**
     * @param keyString - Redis key
     * @return RedisKey
     *
     */
    public static RedisKey of(String... keyString) {
        RedisKey redisKey = new RedisKey(keyString[0]);
        for (int i = 1; i < keyString.length; i++) {
            redisKey.addParam(keyString[i]);
        }
        return redisKey;
    }

    /**
     * @param clazz - RedisEntity class
     * @param param - Key param
     * @return RedisKey
     */
    public static RedisKey of(Class<? extends RedisEntity> clazz, String... param) {
        return new RedisKey(clazz).addParam(param);
    }

}
