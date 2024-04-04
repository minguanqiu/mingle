package io.github.minguanq.mingle.svc.data.aspect;

import io.github.minguanq.mingle.svc.concurrent.SvcAttribute;
import io.github.minguanq.mingle.svc.concurrent.SvcThreadLocal;
import io.github.minguanq.mingle.svc.data.AbstractDao;
import io.github.minguanq.mingle.svc.data.handler.DaoLogHandler;
import io.github.minguanq.mingle.svc.handler.SerialNumberGeneratorHandler;
import io.github.minguanq.mingle.svc.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * {@link Aspect} for dao logging for {@link AbstractDao} all method
 *
 * @author Ming
 */

@Slf4j
@Aspect
public class DaoLogAspect {


    private final DaoLogHandler daoLogHandler;
    private final SerialNumberGeneratorHandler serialNumberGeneratorHandler;

    public DaoLogAspect(DaoLogHandler daoLogHandler, SerialNumberGeneratorHandler serialNumberGeneratorHandler) {
        this.daoLogHandler = daoLogHandler;
        this.serialNumberGeneratorHandler = serialNumberGeneratorHandler;
    }

    @Pointcut("within(io.github.minguanq.mingle.svc.data.AbstractDao+)")
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
                daoLogHandler.writeBeginLog(svcSerialNum, actSerialNum, DateUtils.getNowLocalDateTime(), joinPoint);
            } catch (Exception e) {
                log.error("", e);
            }
            Object proceedObject = joinPoint.proceed();
            try {
                daoLogHandler.writeEndLog(svcSerialNum, actSerialNum, startTime, joinPoint, proceedObject);
            } catch (Exception e) {
                log.error("", e);
            }
            return proceedObject;
        } catch (Throwable t) {
            daoLogHandler.afterThrowing(t, svcSerialNum, actSerialNum, startTime, joinPoint);
            throw t;
        }
    }

}
