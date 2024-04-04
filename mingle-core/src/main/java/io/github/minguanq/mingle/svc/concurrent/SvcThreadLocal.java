package io.github.minguanq.mingle.svc.concurrent;

import java.util.Optional;

/**
 * This class provides inheritable thread local for service,use {@link SvcAttribute} as value
 *
 * @author Ming
 */
public class SvcThreadLocal {

    private final static ThreadLocal<SvcAttribute> INHERITABLE_THREAD_LOCAL = new InheritableThreadLocal<>();

    public static void set(SvcAttribute svcAttribute) {
        INHERITABLE_THREAD_LOCAL.set(svcAttribute);
    }

    public static Optional<SvcAttribute> get() {
        return Optional.ofNullable(INHERITABLE_THREAD_LOCAL.get());
    }

    public static void remove() {
        INHERITABLE_THREAD_LOCAL.remove();
    }

}
