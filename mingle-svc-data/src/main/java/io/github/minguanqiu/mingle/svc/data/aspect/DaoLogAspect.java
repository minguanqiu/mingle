package io.github.minguanqiu.mingle.svc.data.aspect;

import io.github.minguanqiu.mingle.svc.concurrent.SvcAttribute;
import io.github.minguanqiu.mingle.svc.concurrent.SvcThreadLocal;
import io.github.minguanqiu.mingle.svc.data.JPARepositoryDao;
import io.github.minguanqiu.mingle.svc.data.handler.DaoLoggingHandler;
import io.github.minguanqiu.mingle.svc.handler.SerialNumberGeneratorHandler;
import io.github.minguanqiu.mingle.svc.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * {@link Aspect} for dao logging for {@link JPARepositoryDao} all method
 *
 * @author Ming
 */

@Slf4j
@Aspect
public class DaoLogAspect {


    private final DaoLoggingHandler daoLoggingHandler;
    private final SerialNumberGeneratorHandler serialNumberGeneratorHandler;

    public DaoLogAspect(DaoLoggingHandler daoLoggingHandler, SerialNumberGeneratorHandler serialNumberGeneratorHandler) {
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
        Optional<SvcAttribute> svcAttributeOptional = SvcThreadLocal.get();
        if (svcAttributeOptional.isEmpty()) {
            return joinPoint.proceed();
        }
        SvcAttribute svcAttribute = svcAttributeOptional.get();
        String svcSerialNum = (String) svcAttribute.getAttributes(SvcAttribute.Name.SVC_SERIAL_NUMBER);
        String actSerialNum = serialNumberGeneratorHandler.generate("dao");
        LocalDateTime startTime = DateUtils.getNowLocalDateTime();
        try {
            try {
                daoLoggingHandler.writeBeginLog(svcSerialNum, actSerialNum, DateUtils.getNowLocalDateTime(), joinPoint);
            } catch (Exception e) {
                log.error("", e);
            }
            Object proceedObject = joinPoint.proceed();
            try {
                daoLoggingHandler.writeEndLog(svcSerialNum, actSerialNum, startTime, joinPoint, proceedObject);
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
