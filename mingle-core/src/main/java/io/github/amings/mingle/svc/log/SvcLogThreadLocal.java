package io.github.amings.mingle.svc.log;

/**
 * @author Ming
 */
public class SvcLogThreadLocal {

    private final static ThreadLocal<SvcLogModel> svcLogModelThreadLocal = new ThreadLocal<>();

    public static void set(SvcLogModel svcLogModel) {
        svcLogModelThreadLocal.set(svcLogModel);
    }

    public static SvcLogModel get() {
        return svcLogModelThreadLocal.get();
    }

    public static void remove() {
        svcLogModelThreadLocal.remove();
    }

}
