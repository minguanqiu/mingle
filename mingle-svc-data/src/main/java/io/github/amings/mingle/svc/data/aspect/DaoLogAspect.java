package io.github.amings.mingle.svc.data.aspect;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.amings.mingle.svc.data.handler.DaoLogHandler;
import io.github.amings.mingle.svc.data.handler.model.DaoLogBeginModel;
import io.github.amings.mingle.svc.data.handler.model.DaoLogEndModel;
import io.github.amings.mingle.svc.filter.SvcInfo;
import io.github.amings.mingle.svc.log.LogUtils;
import io.github.amings.mingle.svc.log.SvcLogModel;
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
 * Dao logging aspect
 *
 * @author Ming
 */

@Slf4j
@Aspect
public class DaoLogAspect {

    @Autowired
    SvcInfo svcInfo;
    @Autowired
    DaoLogHandler daoLogHandler;
    @Autowired
    @Qualifier("dataLogJacksonUtils")
    JacksonUtils jacksonUtils;

    @Pointcut("within(io.github.amings.mingle.svc.data.dao.AbstractDao+)")
    public void doPoint() {
    }

    @Around("doPoint()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        return processAround(joinPoint);
    }

    private Object processAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String uuid = UUIDUtils.generateUuid();
        LocalDateTime start = DateUtils.getNowLocalDateTime();
        SvcLogModel svcLogModel = LogUtils.getSvcLogModel(svcInfo);
        try {
            try {
                if (svcLogModel != null) {
                    daoLogHandler.writeBeginLog(processBeginLog(svcLogModel, uuid, start, joinPoint));
                }
            } catch (Exception e) {
                log.error("", e);
            }
            Object proceed = joinPoint.proceed();
            try {
                if (svcLogModel != null) {
                    daoLogHandler.writeEndLog(processEndLog(svcLogModel, uuid, start, proceed));
                }
            } catch (Exception e) {
                log.error("", e);
            }
            return proceed;
        } catch (Throwable t) {
            if (svcLogModel != null) {
                daoLogHandler.afterThrowing(t, processEndLog(svcLogModel, uuid, start, null));
            }
            throw t;
        }
    }

    @SuppressWarnings("unchecked")
    private DaoLogBeginModel processBeginLog(SvcLogModel svcLogModel, String uuid, LocalDateTime start, ProceedingJoinPoint joinPoint) {
        DaoLogBeginModel model = new DaoLogBeginModel();
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
                parseObject(reqModel, parameter, object);
            } else if (joinPoint.getArgs()[i] instanceof String) {
                reqModel.put(parameter.getName(), object.toString());
            } else {
                parseObject(reqModel, parameter, object);
            }
        }
        model.setSvcUuid(svcLogModel.getSvcUuid());
        model.setUuid(uuid);
        model.setName(joinPoint.getTarget().getClass().getSimpleName() + "_" + method.getName());
        model.setStartDateTime(start);
        model.setRequestBody(reqModel.toString());
        return model;
    }

    private DaoLogEndModel processEndLog(SvcLogModel svcLogModel, String uuid, LocalDateTime start, Object proceed) {
        DaoLogEndModel model = new DaoLogEndModel();
        LocalDateTime end = DateUtils.getNowLocalDateTime();
        model.setSvcUuid(svcLogModel.getSvcUuid());
        model.setUuid(uuid);
        model.setEndDateTime(end);
        if (proceed != null) {
            Object object = checkOptional(proceed);
            jacksonUtils.readTree(object).ifPresent(node -> model.setResponseBody(node.toString()));
        } else {
            model.setResponseBody(jacksonUtils.getObjectNode().toString());
        }
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

    private void parseObject(ObjectNode reqModel, Parameter parameter, Object object) {
        Optional<JsonNode> jsonNodeOptional = jacksonUtils.readTree(object);
        if (jsonNodeOptional.isPresent()) {
            reqModel.set(parameter.getName(), jsonNodeOptional.get());
        } else {
            reqModel.set(parameter.getName(), null);
        }
    }

}
