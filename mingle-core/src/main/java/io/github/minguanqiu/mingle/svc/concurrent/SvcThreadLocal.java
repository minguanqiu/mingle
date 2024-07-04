package io.github.minguanqiu.mingle.svc.concurrent;

import java.util.Optional;

/**
 * This class provides inheritable thread local for service,use {@link Attribute} as value.
 *
 * @author Qiu Guan Ming
 */
public class SvcThreadLocal {

  private static final ThreadLocal<Attribute> INHERITABLE_THREAD_LOCAL =
      new InheritableThreadLocal<>();

  /**
   * Set attribute into thread local.
   *
   * @param attribute the service attribute.
   */
  public static void set(Attribute attribute) {
    INHERITABLE_THREAD_LOCAL.set(attribute);
  }

  /**
   * Get attribute from thread local.
   *
   * @return return the optional attribute.
   */
  public static Optional<Attribute> get() {
    return Optional.ofNullable(INHERITABLE_THREAD_LOCAL.get());
  }

  /**
   * Remove thread local.
   */
  public static void remove() {
    INHERITABLE_THREAD_LOCAL.remove();
  }

}
