package io.github.amings.mingle.svc.redis.aspect;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.amings.mingle.svc.redis.handler.RedisLogHandler;
import io.github.amings.mingle.svc.redis.handler.model.RedisLogBeginModel;
import io.github.amings.mingle.svc.redis.handler.model.RedisLogEndModel;
import io.github.amings.mingle.utils.DateUtils;
import io.github.amings.mingle.utils.JacksonUtils;
import io.github.amings.mingle.utils.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Redis logging aspect
 *
 * @author Ming
 */

@Slf4j
@Aspect
public class RedisLogAspect {

    @Autowired
    RedisLogHandler redisLogHandler;

    @Autowired
    @Qualifier("redisLogJacksonUtils")
    JacksonUtils jacksonUtils;

    @Pointcut("within(io.github.amings.mingle.svc.redis.Redis+)")
    public void doPoint() {
    }

    @Around("doPoint()")
    public Object around(ProceedingJoinPoint joinPoint) {
        return processAround(joinPoint);
    }

    private Object processAround(ProceedingJoinPoint joinPoint) {
        String uuid = UUIDUtils.generateUuid();
        LocalDateTime start = DateUtils.getNowLocalDateTime();
        try {
            try {
                redisLogHandler.writeBeginLog(processBeginLog(uuid, start, joinPoint));
            } catch (Exception e) {
                log.error("", e);
            }
            Object proceed = joinPoint.proceed();
            try {
                redisLogHandler.writeEndLog(processEndLog(uuid, start, proceed));
            } catch (Exception e) {
                log.error("", e);
            }
            return proceed;
        } catch (Throwable t) {
            LocalDateTime endDateTime = DateUtils.getNowLocalDateTime();
            redisLogHandler.afterThrowing(t, uuid, endDateTime, String.valueOf(Duration.between(start, endDateTime).toMillis()));
            throw new RuntimeException(t);
        }
    }

    private RedisLogBeginModel processBeginLog(String uuid, LocalDateTime start, ProceedingJoinPoint joinPoint) {
        RedisLogBeginModel model = new RedisLogBeginModel();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        ObjectNode reqModel = jacksonUtils.getObjectNode();
        for (int i = 0; i < method.getParameters().length; i++) {
            Parameter parameter = method.getParameters()[i];
            Object object = joinPoint.getArgs()[i];
            if (joinPoint.getArgs()[i] instanceof Optional) {
                Optional<Object> objectOptional = ((Optional<Object>) joinPoint.getArgs()[i]);
                if (objectOptional.isPresent()) {
                    object = objectOptional.get();
                }
                reqModel.set(parameter.getName(), jacksonUtils.readTree(object).get());
            } else if (joinPoint.getArgs()[i] instanceof String) {
                reqModel.put(parameter.getName(), object.toString());
            } else {
                reqModel.set(parameter.getName(), jacksonUtils.readTree(object).get());
            }
        }
        model.setUuid(uuid);
        model.setName(joinPoint.getTarget().getClass().getSimpleName() + "_" + method.getName());
        model.setStartDateTime(start);
        model.setRequestBody(reqModel.toString());
        return model;
    }

    private RedisLogEndModel processEndLog(String uuid, LocalDateTime start, Object proceed) {
        RedisLogEndModel model = new RedisLogEndModel();
        LocalDateTime end = DateUtils.getNowLocalDateTime();
        model.setUuid(uuid);
        model.setEndDateTime(end);
        Object object = checkOptional(proceed);
        jacksonUtils.readTree(object).ifPresent(node -> model.setResponseBody(node.toString()));
        model.setRunTime(String.valueOf(Duration.between(start, end).toMillis()));
        return model;
    }

    @SuppressWarnings("unchecked")
    private Object checkOptional(Object resModel) {
        if (resModel instanceof Optional) {
            Optional<Object> option = (Optional<Object>) resModel;
            return option.orElse(null);
        }
        return resModel;
    }

}
