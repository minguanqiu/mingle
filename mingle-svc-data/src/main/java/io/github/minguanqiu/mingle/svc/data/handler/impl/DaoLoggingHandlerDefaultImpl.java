package io.github.minguanqiu.mingle.svc.data.handler.impl;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.minguanqiu.mingle.svc.data.handler.DaoLoggingHandler;
import io.github.minguanqiu.mingle.svc.utils.DateUtils;
import io.github.minguanqiu.mingle.svc.utils.JacksonUtils;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * {@link DaoLoggingHandler} impl
 *
 * @author Qiu Guan Ming
 */

@Slf4j
public class DaoLoggingHandlerDefaultImpl implements DaoLoggingHandler {

  private final JacksonUtils jacksonUtils;

  public DaoLoggingHandlerDefaultImpl(JacksonUtils jacksonUtils) {
    this.jacksonUtils = jacksonUtils;
  }

  @Override
  public void writeBeginLog(String svcSerialNum, String actSerialNum, LocalDateTime startTime,
      ProceedingJoinPoint joinPoint) {
    ObjectNode objectNode = jacksonUtils.getObjectNode();
    objectNode.put("svcSerialNum", svcSerialNum);
    objectNode.put("actSerialNum", actSerialNum);
    objectNode.put("name",
        joinPoint.getTarget().getClass().getSimpleName() + "." +joinPoint.getSignature().getName());
    objectNode.put("startTime", DateUtils.dateTimeFormat(startTime, "yyyy/MM/dd HH:mm:ss").get());
    objectNode.set("reqBody", jacksonUtils.readTree(buildParameter(joinPoint)).orElse(null));
    objectNode.put("type", "dao");
    log.info("【Dao Request】" + objectNode);
  }

  @Override
  public void writeEndLog(String svcSerialNum, String actSerialNum, LocalDateTime startTime,
      ProceedingJoinPoint joinPoint, Object proceedObject) {
    ObjectNode objectNode = jacksonUtils.getObjectNode();
    LocalDateTime endTime = LocalDateTime.now();
    objectNode.put("svcSerialNum", svcSerialNum);
    objectNode.put("actSerialNum", actSerialNum);
    objectNode.put("endTime", DateUtils.dateTimeFormat(endTime, "yyyy/MM/dd HH:mm:ss").get());
    objectNode.set("reqBody", jacksonUtils.readTree(proceedObject).orElse(null));
    objectNode.put("runTime", DateUtils.between(ChronoUnit.MILLIS, startTime, endTime) + " ms");
    log.info("【Dao Response】" + objectNode);
  }

  @Override
  public void afterThrowing(Throwable throwable, String svcSerialNum, String actSerialNum,
      LocalDateTime startTime, ProceedingJoinPoint joinPoint) {
    ObjectNode objectNode = jacksonUtils.getObjectNode();
    LocalDateTime endTime = LocalDateTime.now();
    objectNode.put("svcSerialNum", svcSerialNum);
    objectNode.put("actSerialNum", actSerialNum);
    objectNode.put("name", joinPoint.getTarget().getClass().getSimpleName() + "." +joinPoint.getSignature().getName());
    objectNode.put("startTime", DateUtils.dateTimeFormat(startTime, "yyyy/MM/dd HH:mm:ss").get());
    objectNode.put("endTime", DateUtils.dateTimeFormat(endTime, "yyyy/MM/dd HH:mm:ss").get());
    objectNode.put("exception", throwable.toString());
    objectNode.put("msg", throwable.getMessage());
    objectNode.put("runTime", DateUtils.between(ChronoUnit.MILLIS, startTime, endTime) + " ms");
    log.info("【Dao Response】" + objectNode);
  }

  private HashMap<String, Object> buildParameter(ProceedingJoinPoint joinPoint) {
    HashMap<String, Object> parameters = new HashMap<>();
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    Method method = signature.getMethod();
    for (int i = 0; i < method.getParameters().length; i++) {
      Parameter parameter = method.getParameters()[i];
      Object object = joinPoint.getArgs()[i];
      parameters.put(parameter.getName(), object);
    }
    return parameters;
  }
}
