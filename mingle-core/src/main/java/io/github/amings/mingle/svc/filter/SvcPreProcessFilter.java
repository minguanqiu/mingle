package io.github.amings.mingle.svc.filter;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.amings.mingle.svc.annotation.Svc;
import io.github.amings.mingle.svc.component.SvcBinderComponent;
import io.github.amings.mingle.svc.exception.ReqBodyNotJsonFormatException;
import io.github.amings.mingle.svc.exception.handler.resolver.ExceptionHandlerResolver;
import io.github.amings.mingle.svc.handler.IPHandler;
import io.github.amings.mingle.svc.handler.PayLoadDecryptionHandler;
import io.github.amings.mingle.svc.handler.SvcLogHandler;
import io.github.amings.mingle.svc.handler.SvcResModelHandler;
import io.github.amings.mingle.svc.handler.model.SvcBeginModel;
import io.github.amings.mingle.svc.handler.model.SvcEndModel;
import io.github.amings.mingle.utils.DateUtils;
import io.github.amings.mingle.utils.JacksonUtils;
import io.github.amings.mingle.utils.ReflectionUtils;
import io.github.amings.mingle.utils.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.ServletRequestPathUtils;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Preprocess Svc filter
 *
 * @author Ming
 */

public class SvcPreProcessFilter extends AbstractSvcFilter {

    @Autowired
    RequestMappingHandlerMapping requestMappingHandlerMapping;
    @Autowired
    PayLoadDecryptionHandler payLoadDecryptionHandler;
    @Autowired
    SvcLogHandler svcLogHandler;
    @Autowired
    ExceptionHandlerResolver exceptionHandlerResolver;
    @Autowired
    IPHandler ipHandler;
    @Autowired
    SvcBinderComponent svcBinderComponent;
    @Autowired
    SvcResModelHandler svcResModelHandler;

    @Autowired
    @Qualifier("svcLogJacksonUtils")
    JacksonUtils jacksonUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        try {
            start(request, response);
            filterChain.doFilter(svcInfo.getHttpServletRequest(), svcInfo.getHttpServletResponse());
        } catch (Exception e) {
            if (!response.isCommitted()) {
                svcInfo.setException(true);
                ResponseEntity<SvcResModelHandler> responseEntity = exceptionHandlerResolver.resolver(e);
                svcInfo.getHttpServletResponse().setStatus(responseEntity.getStatusCodeValue());
                svcInfo.getHttpServletResponse().setCharacterEncoding("UTF-8");
                svcInfo.getHttpServletResponse().setHeader("Content-Type", "application/json");
                svcInfo.setSvcResModelHandler(responseEntity.getBody());
                svcInfo.getHttpServletResponse().resetBuffer();
                svcInfo.getHttpServletResponse().getWriter().write(jacksonUtils.readTree(responseEntity.getBody()).get().toString());
            }
        }
        end();
    }

    private void start(HttpServletRequest request, HttpServletResponse response) throws Exception {
        svcInfo.setHttpServletRequest(request);
        svcInfo.setHttpServletResponse(response);
        svcInfo.setIp(ipHandler.getIP(svcInfo.getHttpServletRequest()));
        HandlerExecutionChain handlerExecutionChain = getHandlerExecutionChain(request);
        Object handler = handlerExecutionChain.getHandler();
        if (handler instanceof HandlerMethod) {
            buildSvcInfo((HandlerMethod) handler);
        }
    }

    private HandlerExecutionChain getHandlerExecutionChain(HttpServletRequest request) throws Exception {
        if (!ServletRequestPathUtils.hasParsedRequestPath(request)) {
            ServletRequestPathUtils.parseAndCache(request);
        }
        return requestMappingHandlerMapping.getHandler(request);
    }

    private void buildSvcInfo(HandlerMethod handlerMethod) {
        Optional<SvcBinderComponent.SvcBinderModel> svcBinderModelOptional = svcBinderComponent.getSvcBinderModel(handlerMethod.getBeanType());
        if (svcBinderModelOptional.isPresent()) {
            SvcBinderComponent.SvcBinderModel svcBinderModel = svcBinderModelOptional.get();
            Svc svc = svcBinderModel.getSvc();
            svcInfo.setSvcBinderModel(svcBinderModel);
            svcInfo.setSvcUuid(UUIDUtils.generateUuid());
            svcInfo.setSvcName(svcBinderModel.getSvcName());
            if (!svcBinderModel.isReqCustom()) {
                ContentCachingRequestWrapper reqWrapper = new ContentCachingRequestWrapper(svcInfo.getHttpServletRequest());
                processSvcRequest(reqWrapper, svc);
                svcInfo.setHttpServletRequest(reqWrapper);
            }
            if (!svcBinderModel.isResCustom()) {
                svcInfo.setHttpServletResponse(new ContentCachingResponseWrapper(svcInfo.getHttpServletResponse()));
            }
        }
    }

    private void processSvcRequest(ContentCachingRequestWrapper reqWrapper, Svc svcAnnotation) {
        String body = getBody(reqWrapper);
        String payLoadBody = svcAnnotation.encryption() ? decryption(body) : body;
        Optional<JsonNode> jsonNodeOptional = jacksonUtils.readTree(payLoadBody);
        if (!jsonNodeOptional.isPresent()) {
            throw new ReqBodyNotJsonFormatException();
        }
        JsonNode payloadNode = jsonNodeOptional.get();
        svcInfo.setPayLoadNode(payloadNode);
        svcInfo.setPayLoadString(payloadNode.toString());
    }

    private String decryption(String body) {
        return payLoadDecryptionHandler.decryption(body);
    }

    private String getBody(ContentCachingRequestWrapper reqWrapper) {
        String body = null;
        try (BufferedReader bufReader = new BufferedReader(new InputStreamReader(reqWrapper.getInputStream()))) {
            body = bufReader.lines().collect(Collectors.joining());
        } catch (IOException ignored) {
        }
        return body;
    }

    private void end() throws IOException {
        if (!svcInfo.isException()) {
            if (!svcInfo.getSvcBinderModel().isResCustom()) {
                if (svcInfo.getHttpServletResponse().getClass().equals(ContentCachingResponseWrapper.class)) {
                    ContentCachingResponseWrapper httpServletResponse = (ContentCachingResponseWrapper) svcInfo.getHttpServletResponse();
                    String responseBody = new String(httpServletResponse.getContentAsByteArray(), StandardCharsets.UTF_8);
                    JsonNode jsonNode = jacksonUtils.readTree(responseBody).get();
                    SvcResModelHandler svcResModelHandlerImpl = ReflectionUtils.newInstance(svcResModelHandler.getClass());
                    svcResModelHandlerImpl.setCode(svcInfo.getCode());
                    svcResModelHandlerImpl.setDesc(svcInfo.getDesc());
                    svcResModelHandlerImpl.setResBody(jsonNode);
                    svcInfo.setSvcResModelHandler(svcResModelHandlerImpl);
                    httpServletResponse.resetBuffer();
                    httpServletResponse.getWriter().write(jacksonUtils.readTree(svcResModelHandlerImpl).get().toString());
                    httpServletResponse.copyBodyToResponse();
                }
            }
        }
        if (svcInfo.getSvcBinderModel().getSvc().log()) {
            if (svcInfo.getSvcBinderModel().isReqCustom()) {
                svcLogHandler.writeBeginLog(buildSvcBeginModel());
            }
            svcLogHandler.writeEndLog(buildSvcEndModel());
        }
    }

    private SvcBeginModel buildSvcBeginModel() {
        SvcBeginModel model = new SvcBeginModel();
        model.setUuid(svcInfo.getUuid());
        model.setName(svcInfo.getSvcName());
        model.setStartDateTime(svcInfo.getStartDateTime());
        jacksonUtils.readTree(svcInfo.getBackReqModel()).ifPresent(jsonNode -> model.setModelBody(jsonNode.toString()));
        model.setIp(svcInfo.getIp());
        return model;
    }

    private SvcEndModel buildSvcEndModel() {
        LocalDateTime endDateTime = DateUtils.getNowLocalDateTime();
        SvcEndModel model = new SvcEndModel();
        model.setUuid(svcInfo.getUuid());
        model.setEndDateTime(endDateTime);
        model.setCode(svcInfo.getCode());
        model.setDesc(svcInfo.getDesc());
        SvcResModelHandler svcResModelHandler = svcInfo.getSvcResModelHandler4Log() == null ? svcInfo.getSvcResModelHandler() : svcInfo.getSvcResModelHandler4Log();
        if (svcResModelHandler != null) {
            jacksonUtils.readTree(svcResModelHandler).ifPresent(jsonNode -> model.setResponseBody(jsonNode.toString()));
            model.setCode(svcResModelHandler.getCode());
            model.setDesc(svcResModelHandler.getDesc());
        }
        model.setRunTime(String.valueOf(DateUtils.between(ChronoUnit.MILLIS, svcInfo.getStartDateTime(), endDateTime)));
        return model;
    }

}
