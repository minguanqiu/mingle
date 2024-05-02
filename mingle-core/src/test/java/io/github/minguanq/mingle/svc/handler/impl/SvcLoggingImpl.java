package io.github.minguanq.mingle.svc.handler.impl;

import io.github.minguanq.mingle.svc.filter.SvcInfo;
import io.github.minguanq.mingle.svc.handler.SvcLoggingHandler;
import org.springframework.stereotype.Component;

/**
 * @author Ming
 */
@Component
public class SvcLoggingImpl implements SvcLoggingHandler {
    @Override
    public void writeBeginLog(SvcInfo svcInfo) {
        svcInfo.getHttpServletRequest().setAttribute(SvcLoggingHandler.class.getSimpleName(),"true");
    }

    @Override
    public void writeEndLog(SvcInfo svcInfo) {
        svcInfo.getHttpServletRequest().setAttribute(SvcLoggingHandler.class.getSimpleName(),"true");
    }

}
