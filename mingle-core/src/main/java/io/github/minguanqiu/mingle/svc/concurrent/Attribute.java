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

  private final HashMap<String, Object> values = new HashMap<>();

  public void setAttributes(String name, Object value) {
    this.values.put(name, value);
  }

  @SuppressWarnings("unchecked")
  public <T> Optional<T> getAttributes(String name) {
    return Optional.ofNullable((T) values.get(name));
  }

}
