package io.github.amings.mingle.svc.aspect;

import io.github.amings.mingle.svc.action.AbstractAction;
import io.github.amings.mingle.svc.action.ActionReqModel;
import io.github.amings.mingle.svc.action.ActionResData;
import io.github.amings.mingle.svc.exception.ActionAutoBreakException;
import io.github.amings.mingle.svc.filter.SvcInfo;
import io.github.amings.mingle.svc.handler.ActionLogHandler;
import io.github.amings.mingle.svc.handler.model.ActionBeginModel;
import io.github.amings.mingle.svc.handler.model.ActionEndModel;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Action logging aspect
 *
 * @author Ming
 */

@Slf4j
@Aspect
public class ActionLogAspect {

    @Autowired
    SvcInfo svcInfo;
    @Autowired
    ActionLogHandler actionLogHandler;
    @Autowired
    @Qualifier("actionLogJacksonUtils")
    JacksonUtils jacksonUtils;

    @Pointcut("execution(* io.github.amings.mingle.svc.action.AbstractAction+.doAction(io.github.amings.mingle.svc.action.ActionReqModel))")
    public void doPoint() {
    }

    @Around("doPoint() && args(reqModel)")
    public Object around(ProceedingJoinPoint joinPoint, ActionReqModel reqModel) {
        return processAround(joinPoint, reqModel);
    }

    private Object processAround(ProceedingJoinPoint joinPoint, ActionReqModel reqModel) {
        String uuid = UUIDUtils.generateUuidRandom();
        LocalDateTime startDateTime = DateUtils.getNowLocalDateTime();
        SvcLogModel svcLogModel = LogUtils.getSvcLogModel(svcInfo);
        try {
            try {
                if (svcLogModel != null) {
                    actionLogHandler.writeBeginLog(buildActionBeginModel(joinPoint, svcLogModel, uuid, startDateTime, reqModel));
                }
            } catch (Exception e) {
                log.error("", e);
            }
            Object proceed = joinPoint.proceed();
            try {
                if (svcLogModel != null) {
                    actionLogHandler.writeEndLog(buildActionEndModel(svcLogModel, uuid, startDateTime, (ActionResData<?>) proceed));
                }
            } catch (Exception e) {
                log.error("", e);
            }
            return proceed;
        } catch (ActionAutoBreakException e) {
            if (svcLogModel != null) {
                actionLogHandler.writeEndLog(buildActionEndModel(svcLogModel, uuid, startDateTime, e.getActionResData()));
            }
            throw e;
        } catch (Throwable t) {
            if (svcLogModel != null) {
                actionLogHandler.afterThrowing(t, buildActionEndModel(svcLogModel, uuid, startDateTime, null));
            }
            throw new RuntimeException(t);
        }
    }

    private ActionBeginModel buildActionBeginModel(ProceedingJoinPoint joinPoint, SvcLogModel svcLogModel, String uuid, LocalDateTime startDateTime, ActionReqModel reqModel) {
        ActionBeginModel model = new ActionBeginModel();
        model.setSvcUuid(svcLogModel.getSvcUuid());
        model.setUuid(uuid);
        model.setName(this.getClass().getSimpleName());
        model.setStartDateTime(startDateTime);
        jacksonUtils.readTree(reqModel).ifPresent(node -> model.setRequestBody(node.toString()));
        AbstractAction target = (AbstractAction) joinPoint.getTarget();
        model.setType(target.getType());
        return model;
    }

    private ActionEndModel buildActionEndModel(SvcLogModel svcLogModel, String uuid, LocalDateTime startDateTime, ActionResData<?> actionResData) {
        LocalDateTime endDateTime = DateUtils.getNowLocalDateTime();
        ActionEndModel model = new ActionEndModel();
        model.setSvcUuid(svcLogModel.getSvcUuid());
        model.setUuid(uuid);
        model.setEndDateTime(endDateTime);
        if (actionResData != null) {
            jacksonUtils.readTree(actionResData.getResModel()).ifPresent(node -> model.setResponseBody(node.toString()));
            model.setCode(actionResData.getCode());
            model.setDesc(actionResData.getDesc());
        }
        model.setRunTime(String.valueOf(DateUtils.between(ChronoUnit.MILLIS, startDateTime, endDateTime)));
        return model;
    }

}
