package io.github.minguanqiu.mingle.svc.data.handler.impl;

import io.github.minguanqiu.mingle.svc.data.handler.DaoLoggingHandler;
import io.github.minguanqiu.mingle.svc.filter.SvcInfo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author Ming
 */
@Component
public class DaoLoggingHandlerImpl implements DaoLoggingHandler {
    @Autowired
    SvcInfo svcInfo;

    @Override
    public void writeBeginLog(String svcSerialNum, String actSerialNum, LocalDateTime startDateTime, ProceedingJoinPoint joinPoint) {
        svcInfo.getHttpServletRequest().setAttribute(DaoLoggingHandler.class.getSimpleName(), "true");
    }

    @Override
    public void writeEndLog(String svcSerialNum, String actSerialNum, LocalDateTime startDateTime, ProceedingJoinPoint joinPoint, Object proceedObject) {
        svcInfo.getHttpServletRequest().setAttribute(DaoLoggingHandler.class.getSimpleName(), "true");
    }

    @Override
    public void afterThrowing(Throwable throwable, String svcSerialNum, String actSerialNum, LocalDateTime startTime, ProceedingJoinPoint joinPoint) {
        svcInfo.getHttpServletRequest().setAttribute(DaoLoggingHandler.class.getSimpleName(), "true");
    }
}
