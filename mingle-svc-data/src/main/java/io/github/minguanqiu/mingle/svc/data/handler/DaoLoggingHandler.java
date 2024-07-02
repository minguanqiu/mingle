package io.github.minguanqiu.mingle.svc.data.handler;

import java.time.LocalDateTime;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * Handler for dao logging
 *
 * @author Qiu Guan Ming
 */

public interface DaoLoggingHandler {

  void writeBeginLog(String svcSerialNum, String actSerialNum, LocalDateTime startDateTime,
      ProceedingJoinPoint joinPoint);

  void writeEndLog(String svcSerialNum, String actSerialNum, LocalDateTime startDateTime,
      ProceedingJoinPoint joinPoint, Object proceedObject);

  void afterThrowing(Throwable throwable, String svcSerialNum, String actSerialNum,
      LocalDateTime startTime, ProceedingJoinPoint joinPoint);


}
