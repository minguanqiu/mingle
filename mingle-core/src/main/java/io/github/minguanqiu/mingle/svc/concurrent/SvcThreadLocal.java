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

  public static void set(Attribute attribute) {
    INHERITABLE_THREAD_LOCAL.set(attribute);
  }

  public static Optional<Attribute> get() {
    return Optional.ofNullable(INHERITABLE_THREAD_LOCAL.get());
  }

  public static void remove() {
    INHERITABLE_THREAD_LOCAL.remove();
  }

}
