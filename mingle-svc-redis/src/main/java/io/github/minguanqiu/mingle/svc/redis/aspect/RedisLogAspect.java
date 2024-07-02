package io.github.minguanqiu.mingle.svc.redis.aspect;

import io.github.minguanqiu.mingle.svc.concurrent.Attribute;
import io.github.minguanqiu.mingle.svc.concurrent.SvcAttributeName;
import io.github.minguanqiu.mingle.svc.concurrent.SvcThreadLocal;
import io.github.minguanqiu.mingle.svc.handler.SerialNumberGeneratorHandler;
import io.github.minguanqiu.mingle.svc.redis.RedisTemplateDao;
import io.github.minguanqiu.mingle.svc.redis.handler.RedisLogHandler;
import io.github.minguanqiu.mingle.svc.utils.DateUtils;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * {@link Aspect} for redis logging for {@link RedisTemplateDao} all method
 *
 * @author Qiu Guan Ming
 */

@Slf4j
@Aspect
public class RedisLogAspect {

  private final RedisLogHandler redisLogHandler;

  private final SerialNumberGeneratorHandler serialNumberGeneratorHandler;

  public RedisLogAspect(RedisLogHandler redisLogHandler,
      SerialNumberGeneratorHandler serialNumberGeneratorHandler) {
    this.redisLogHandler = redisLogHandler;
    this.serialNumberGeneratorHandler = serialNumberGeneratorHandler;
  }

  @Pointcut("within(io.github.minguanqiu.mingle.svc.redis.RedisDao+)")
  public void doPoint() {
  }

  @Around("doPoint()")
  public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
    return processAround(joinPoint);
  }

  private Object processAround(ProceedingJoinPoint joinPoint) throws Throwable {
    Optional<Attribute> svcAttributeOptional = SvcThreadLocal.get();
    if (svcAttributeOptional.isEmpty()) {
      return joinPoint.proceed();
    }
    Attribute attribute = svcAttributeOptional.get();
    String svcSerialNum = (String) attribute.getAttributes(SvcAttributeName.SVC_SERIAL_NUMBER).get();
    String actSerialNum = serialNumberGeneratorHandler.generate("redis");
    LocalDateTime startTime = DateUtils.getNowLocalDateTime();
    try {
      try {
        redisLogHandler.writeBeginLog(svcSerialNum, actSerialNum, DateUtils.getNowLocalDateTime(),
            joinPoint);
      } catch (Exception e) {
        log.error("", e);
      }
      Object proceedObject = joinPoint.proceed();
      try {
        redisLogHandler.writeEndLog(svcSerialNum, actSerialNum, startTime, joinPoint,
            proceedObject);
      } catch (Exception e) {
        log.error("", e);
      }
      return proceedObject;
    } catch (Throwable t) {
      redisLogHandler.afterThrowing(t, svcSerialNum, actSerialNum, startTime, joinPoint);
      throw t;
    }
  }

}
