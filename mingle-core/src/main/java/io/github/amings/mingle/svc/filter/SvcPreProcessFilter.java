package io.github.amings.mingle.svc.filter;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.amings.mingle.svc.annotation.Svc;
import io.github.amings.mingle.svc.component.SvcBinderComponent;
import io.github.amings.mingle.svc.config.properties.SvcProperties;
import io.github.amings.mingle.svc.exception.IPAuthenticationFailException;
import io.github.amings.mingle.svc.exception.ReqBodyNotJsonFormatException;
import io.github.amings.mingle.svc.exception.SvcNotFoundException;
import io.github.amings.mingle.svc.exception.handler.resolver.ExceptionHandlerResolver;
import io.github.amings.mingle.svc.handler.*;
import io.github.amings.mingle.svc.handler.model.SvcEndModel;
import io.github.amings.mingle.utils.DateUtils;
import io.github.amings.mingle.utils.JacksonUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

    private final PayLoadDecryptionHandler payLoadDecryptionHandler;
    private final SvcLogHandler svcLogHandler;
    private final ExceptionHandlerResolver exceptionHandlerResolver;
    private final IPHandler ipHandler;
    private final SvcBinderComponent svcBinderComponent;
    private final JacksonUtils jacksonUtils;

    public SvcPreProcessFilter(SvcInfo svcInfo, SvcMsgHandler svcMsgHandler, SvcProperties svcProperties, PayLoadDecryptionHandler payLoadDecryptionHandler, SvcLogHandler svcLogHandler, ExceptionHandlerResolver exceptionHandlerResolver, IPHandler ipHandler, SvcBinderComponent svcBinderComponent, JacksonUtils jacksonUtils) {
        super(svcInfo, svcMsgHandler, svcProperties);
        this.payLoadDecryptionHandler = payLoadDecryptionHandler;
        this.svcLogHandler = svcLogHandler;
        this.exceptionHandlerResolver = exceptionHandlerResolver;
        this.ipHandler = ipHandler;
        this.svcBinderComponent = svcBinderComponent;
        this.jacksonUtils = jacksonUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        try {
            svcInfo.setHttpServletRequest(request);
            svcInfo.setHttpServletResponse(response);
            response.setCharacterEncoding("UTF-8");
            Optional<SvcBinderComponent.SvcBinderModel> optionalSvcBinderModel = svcBinderComponent.getSvcBinderModel(request.getServletPath());
            if (optionalSvcBinderModel.isPresent()) {
                svcInfo.setSvcBinderModel(optionalSvcBinderModel.get());
                start(request);
            }
            filterChain.doFilter(svcInfo.getHttpServletRequest(), svcInfo.getHttpServletResponse());
        } catch (Exception e) {
            if (!response.isCommitted()) {
                svcInfo.setException(true);
                ResponseEntity<SvcResModelHandler> responseEntity = exceptionHandlerResolver.resolver(e);
                svcInfo.getHttpServletResponse().setStatus(responseEntity.getStatusCode().value());
                svcInfo.getHttpServletResponse().setHeader("Content-Type", "application/json");
                svcInfo.setSvcResModelHandler(responseEntity.getBody());
                svcInfo.getHttpServletResponse().resetBuffer();
                svcInfo.getHttpServletResponse().getWriter().write(jacksonUtils.readTree(responseEntity.getBody()).get().toString());
            }
        }
        if (svcInfo.getSvcBinderModel() != null) {
            if (svcInfo.getSvcBinderModel().getSvc().log() && svcInfo.isWriteBegin()) {
                svcLogHandler.writeEndLog(buildSvcEndModel());
            }
        }
    }

    private void start(HttpServletRequest request) {
        svcInfo.setCode(svcProperties.getSuccessCode());
        svcInfo.setDesc(svcProperties.getSuccessDesc());
        String comeFromIp = ipHandler.getIP(svcInfo.getHttpServletRequest());
        svcInfo.setIp(comeFromIp);
        Optional<SvcBinderComponent.SvcBinderModel> optionalSvcBinderModel = svcBinderComponent.getSvcBinderModel(request.getServletPath());
        if (optionalSvcBinderModel.isEmpty()) {
            throw new SvcNotFoundException("Can't found Svc in SvcBinderModel");
        }
        SvcBinderComponent.SvcBinderModel svcBinderModel = svcInfo.getSvcBinderModel();
        svcInfo.setSvcName(svcBinderModel.getSvcName());
        if (svcBinderModel.getSvc().ipSecure()) {
            if (!checkIpAddress(svcBinderModel.getIpSecureList(), comeFromIp)) {
                throw new IPAuthenticationFailException("IP Authentication Fail");
            }
        }
        if (!svcBinderModel.isReqCustom()) {
            ContentCachingRequestWrapper contentCachingRequestWrapper = new ContentCachingRequestWrapper(request);
            svcInfo.setHttpServletRequest(contentCachingRequestWrapper);
            processSvcRequest(contentCachingRequestWrapper, svcBinderModel.getSvc());
        }
    }


    private boolean checkIpAddress(String[] ips, String comeFromIp) {
        for (String ip : ips) {
            if (ip.equals(comeFromIp)) {
                return true;
            }
        }
        return false;
    }

    private void processSvcRequest(ContentCachingRequestWrapper reqWrapper, Svc svcAnnotation) {
        String body = getBody(reqWrapper);
        String payLoadBody = svcAnnotation.encryption() ? decryption(body) : body;
        Optional<JsonNode> jsonNodeOptional = jacksonUtils.readTree(payLoadBody);
        if (jsonNodeOptional.isEmpty()) {
            throw new ReqBodyNotJsonFormatException("Request body not json format");
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
