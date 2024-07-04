package io.github.minguanqiu.mingle.svc.redis;

import java.util.ArrayList;
import java.util.List;
import org.springframework.util.Assert;

/**
 * This class provide redis key generation.
 *
 * @author Qiu Guan Ming
 */
public class RedisKey {

  /**
   * Key delimiter.
   */
  public static final String KEY_DELIMITER = ":";

  private final List<String> key;

  /**
   * Create a new RedisKey instance.
   *
   * @param keyParams the key parameters.
   */
  public RedisKey(List<String> keyParams) {
    this.key = keyParams;
  }

  /**
   * Create a new RedisKey builder.
   *
   * @return return the builder.
   */
  public static RedisKeyBuilder builder() {
    return new RedisKeyBuilder();
  }

  @Override
  public String toString() {
    return String.join(KEY_DELIMITER, key);
  }

  /**
   * Redis key builder.
   */
  public static class RedisKeyBuilder {

    private final List<String> params = new ArrayList<>();

    /**
     * Add key parameter.
     *
     * @param param the key parameter.
     * @return return RedisKeyBuilder.
     */
    public RedisKeyBuilder addParam(String param) {
      params.add(param);
      return this;
    }

    /**
     * Create RedisKey instance.
     *
     * @return return the redis instance.
     */
    public RedisKey build() {
      Assert.notEmpty(params, "key params is empty");
      return new RedisKey(params);
    }

  }

}
