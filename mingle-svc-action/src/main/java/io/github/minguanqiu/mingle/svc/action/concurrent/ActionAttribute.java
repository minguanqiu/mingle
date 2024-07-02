package io.github.minguanqiu.mingle.svc.action.concurrent;

import java.util.HashMap;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;

/**
 * This class provide map to set attributes
 *
 * @author Qiu Guan Ming
 */
@Getter
@Setter
public class ActionAttribute {

  private final HashMap<String, Object> values = new HashMap<>();

  public void setAttributes(String name, Object value) {
    this.values.put(name, value);
  }

  public Optional<Object> getAttributes(String name) {
    return Optional.ofNullable(values.get(name));
  }

  public static class Name {

    public static final String ACTION_PROCESS_STATUS = "ACTION_PROCESS_STATUS";

  }

}
