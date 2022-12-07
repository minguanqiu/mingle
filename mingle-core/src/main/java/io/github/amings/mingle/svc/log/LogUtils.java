package io.github.amings.mingle.svc.log;

import io.github.amings.mingle.svc.filter.SvcInfo;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * @author Ming
 */
public class LogUtils {

    public static SvcLogModel getSvcLogModel(SvcInfo svcInfo) {
        if (RequestContextHolder.getRequestAttributes() != null) {
            if (svcInfo.getSvcBinderModel().getSvc().log()) {
                SvcLogModel model = new SvcLogModel();
                model.setSvcUuid(svcInfo.getSvcUuid());
                model.setIp(svcInfo.getIp());
                return model;
            }
        } else {
            if (SvcLogThreadLocal.get() != null) {
                return SvcLogThreadLocal.get();
            }
        }
        return null;
    }
}
