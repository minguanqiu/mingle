package io.github.minguanq.mingle.svc.data.handler;

import org.aspectj.lang.ProceedingJoinPoint;

import java.time.LocalDateTime;

/**
 * Handler for dao logging
 *
 * @author Ming
 */

public interface DaoLoggingHandler {

    void writeBeginLog(String svcSerialNum, String actSerialNum, LocalDateTime startDateTime, ProceedingJoinPoint joinPoint);

    void writeEndLog(String svcSerialNum, String actSerialNum, LocalDateTime startDateTime, ProceedingJoinPoint joinPoint, Object proceedObject);

    void afterThrowing(Throwable throwable, String svcSerialNum, String actSerialNum, LocalDateTime startTime, ProceedingJoinPoint joinPoint);


}
