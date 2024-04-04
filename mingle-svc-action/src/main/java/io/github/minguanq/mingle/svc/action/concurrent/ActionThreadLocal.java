package io.github.minguanq.mingle.svc.action.concurrent;

/**
 * This class provides inheritable thread local for action,use {@link ActionAttribute} as value
 *
 * @author Ming
 */
public class ActionThreadLocal {

    private final static ThreadLocal<ActionAttribute> ACTION_ATTRIBUTE_INHERITABLE_THREAD_LOCAL = new InheritableThreadLocal<>();

    public static void set(ActionAttribute actionAttribute) {
        ACTION_ATTRIBUTE_INHERITABLE_THREAD_LOCAL.set(actionAttribute);
    }

    public static ActionAttribute get() {
        return ACTION_ATTRIBUTE_INHERITABLE_THREAD_LOCAL.get();
    }

    public static void remove() {
        ACTION_ATTRIBUTE_INHERITABLE_THREAD_LOCAL.remove();
    }

}
