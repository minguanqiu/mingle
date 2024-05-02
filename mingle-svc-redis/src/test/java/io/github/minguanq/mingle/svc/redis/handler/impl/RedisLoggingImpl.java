package io.github.minguanq.mingle.svc.redis.handler.impl;

import io.github.minguanq.mingle.svc.filter.SvcInfo;
import io.github.minguanq.mingle.svc.redis.handler.RedisLogHandler;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author Ming
 */
@Component
public class RedisLoggingImpl implements RedisLogHandler {

    @Autowired
    SvcInfo svcInfo;

    @Override
    public void writeBeginLog(String svcSerialNum, String actSerialNum, LocalDateTime startDateTime, ProceedingJoinPoint joinPoint) {
        svcInfo.getHttpServletRequest().setAttribute(RedisLogHandler.class.getSimpleName(), "true");
    }

    @Override
    public void writeEndLog(String svcSerialNum, String actSerialNum, LocalDateTime startDateTime, ProceedingJoinPoint joinPoint, Object proceedObject) {
        svcInfo.getHttpServletRequest().setAttribute(RedisLogHandler.class.getSimpleName(), "true");
    }

    @Override
    public void afterThrowing(Throwable throwable, String svcSerialNum, String actSerialNum, LocalDateTime startTime, ProceedingJoinPoint joinPoint) {
        svcInfo.getHttpServletRequest().setAttribute(RedisLogHandler.class.getSimpleName(), "true");
    }

}
