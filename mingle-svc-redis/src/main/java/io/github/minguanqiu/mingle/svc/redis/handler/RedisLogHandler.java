package io.github.minguanqiu.mingle.svc.redis.handler;

import java.time.LocalDateTime;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * Handler for redis logging.
 *
 * @author Qiu Guan Ming
 */

public interface RedisLogHandler {

  /**
   * Pre-processing logging for redis.
   *
   * @param svcSerialNum  the service serial number.
   * @param actSerialNum  the action serial number.
   * @param startDateTime the action start date time.
   * @param joinPoint     the aop join point.
   */
  void writeBeginLog(String svcSerialNum, String actSerialNum, LocalDateTime startDateTime,
      ProceedingJoinPoint joinPoint);

  /**
   * Post-processing logging for dao.
   *
   * @param svcSerialNum  the service serial number.
   * @param actSerialNum  the action serial number.
   * @param startDateTime the action start date time.
   * @param joinPoint     the aop join point.
   * @param proceedObject the action response object.
   */
  void writeEndLog(String svcSerialNum, String actSerialNum, LocalDateTime startDateTime,
      ProceedingJoinPoint joinPoint, Object proceedObject);

  /**
   * Processing logging for error.
   *
   * @param throwable     the exception.
   * @param svcSerialNum  the service serial number.
   * @param actSerialNum  the action serial number.
   * @param startDateTime the action start date time.
   * @param joinPoint     the aop join point.
   */
  void afterThrowing(Throwable throwable, String svcSerialNum, String actSerialNum,
      LocalDateTime startDateTime, ProceedingJoinPoint joinPoint);

}
