package io.github.amings.mingle.svc.action.logging;

public class ActionLogThreadLocal {

    private final static ThreadLocal<ActionLogModel> actLogModelThreadLocal = new ThreadLocal<>();

    public static void set(ActionLogModel svcLogModel) {
        actLogModelThreadLocal.set(svcLogModel);
    }

    public static ActionLogModel get() {
        return actLogModelThreadLocal.get();
    }

    public static void remove() {
        actLogModelThreadLocal.remove();
    }

}
