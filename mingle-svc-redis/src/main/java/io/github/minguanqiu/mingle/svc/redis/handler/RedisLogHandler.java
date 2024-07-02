package io.github.minguanqiu.mingle.svc.redis.handler;

import java.time.LocalDateTime;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * Handler for redis logging
 *
 * @author Qiu Guan Ming
 */

public interface RedisLogHandler {

  void writeBeginLog(String svcSerialNum, String actSerialNum, LocalDateTime startDateTime,
      ProceedingJoinPoint joinPoint);

  void writeEndLog(String svcSerialNum, String actSerialNum, LocalDateTime startDateTime,
      ProceedingJoinPoint joinPoint, Object proceedObject);

  void afterThrowing(Throwable throwable, String svcSerialNum, String actSerialNum,
      LocalDateTime startTime, ProceedingJoinPoint joinPoint);

}
