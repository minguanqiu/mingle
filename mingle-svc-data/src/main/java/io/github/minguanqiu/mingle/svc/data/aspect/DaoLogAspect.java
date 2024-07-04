package io.github.minguanqiu.mingle.svc.data.aspect;

import io.github.minguanqiu.mingle.svc.concurrent.Attribute;
import io.github.minguanqiu.mingle.svc.concurrent.SvcAttributeName;
import io.github.minguanqiu.mingle.svc.concurrent.SvcThreadLocal;
import io.github.minguanqiu.mingle.svc.data.JPARepositoryDao;
import io.github.minguanqiu.mingle.svc.data.handler.DaoLoggingHandler;
import io.github.minguanqiu.mingle.svc.handler.SerialNumberGeneratorHandler;
import io.github.minguanqiu.mingle.svc.utils.DateUtils;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * {@link Aspect} for dao logging pointcut {@link JPARepositoryDao} all method.
 *
 * @author Qiu Guan Ming
 */

@Slf4j
@Aspect
public class DaoLogAspect {


  private final DaoLoggingHandler daoLoggingHandler;
  private final SerialNumberGeneratorHandler serialNumberGeneratorHandler;

  /**
   * Create a new DaoLogAspect instance.
   *
   * @param daoLoggingHandler            the dao logging handler.
   * @param serialNumberGeneratorHandler the serial number generator handler.
   */
  public DaoLogAspect(DaoLoggingHandler daoLoggingHandler,
      SerialNumberGeneratorHandler serialNumberGeneratorHandler) {
    this.daoLoggingHandler = daoLoggingHandler;
    this.serialNumberGeneratorHandler = serialNumberGeneratorHandler;
  }

  @Pointcut("within(io.github.minguanqiu.mingle.svc.data.JPARepositoryDao+)")
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
    String svcSerialNum = (String) attribute.getAttributes(SvcAttributeName.SVC_SERIAL_NUMBER)
        .get();
    String actSerialNum = serialNumberGeneratorHandler.generate("dao");
    LocalDateTime startTime = DateUtils.getNowLocalDateTime();
    try {
      try {
        daoLoggingHandler.writeBeginLog(svcSerialNum, actSerialNum, DateUtils.getNowLocalDateTime(),
            joinPoint);
      } catch (Exception e) {
        log.error("", e);
      }
      Object proceedObject = joinPoint.proceed();
      try {
        daoLoggingHandler.writeEndLog(svcSerialNum, actSerialNum, startTime, joinPoint,
            proceedObject);
      } catch (Exception e) {
        log.error("", e);
      }
      return proceedObject;
    } catch (Throwable t) {
      daoLoggingHandler.afterThrowing(t, svcSerialNum, actSerialNum, startTime, joinPoint);
      throw t;
    }
  }

}
