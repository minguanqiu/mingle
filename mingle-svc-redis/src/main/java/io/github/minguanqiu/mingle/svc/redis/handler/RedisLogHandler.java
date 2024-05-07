package io.github.minguanqiu.mingle.svc.redis.handler;

import org.aspectj.lang.ProceedingJoinPoint;

import java.time.LocalDateTime;

/**
 * Handler for redis logging
 *
 * @author Ming
 */

public interface RedisLogHandler {

    void writeBeginLog(String svcSerialNum, String actSerialNum, LocalDateTime startDateTime, ProceedingJoinPoint joinPoint);

    void writeEndLog(String svcSerialNum, String actSerialNum, LocalDateTime startDateTime, ProceedingJoinPoint joinPoint, Object proceedObject);

    void afterThrowing(Throwable throwable, String svcSerialNum, String actSerialNum, LocalDateTime startTime, ProceedingJoinPoint joinPoint);

}
