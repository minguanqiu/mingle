package io.github.minguanqiu.mingle.svc.concurrent;

import java.util.HashMap;
import java.util.Optional;
import lombok.Getter;

/**
 * This class provide map to set attributes.
 *
 * @author Qiu Guan Ming
 */
@Getter
public class Attribute {

  /**
   * Map of attributes.
   *
   * @return return the map of attributes.
   */
  private final HashMap<String, Object> values = new HashMap<>();

  /**
   * Save key value attribute.
   *
   * @param key   the key name.
   * @param value the object.
   */
  public void setAttributes(String key, Object value) {
    this.values.put(key, value);
  }

  /**
   * Save key value attribute.
   *
   * @param name the attributes name.
   * @param <T>  return type.
   * @return return the optional object.
   */
  @SuppressWarnings("unchecked")
  public <T> Optional<T> getAttributes(String name) {
    return Optional.ofNullable((T) values.get(name));
  }

}
