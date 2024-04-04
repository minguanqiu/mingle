package io.github.minguanq.mingle.svc.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.minguanq.mingle.svc.SvcResponse;
import io.github.minguanq.mingle.svc.SvcResponseHeader;
import io.github.minguanq.mingle.svc.component.SvcBinderComponent;
import io.github.minguanq.mingle.svc.concurrent.SvcThreadLocal;
import io.github.minguanq.mingle.svc.configuration.properties.SvcProperties;
import io.github.minguanq.mingle.svc.exception.SvcNotFoundException;
import io.github.minguanq.mingle.svc.exception.handler.resolver.ExceptionHandlerResolver;
import io.github.minguanq.mingle.svc.handler.SvcLogHandler;
import io.github.minguanq.mingle.svc.handler.SvcMsgHandler;
import io.github.minguanq.mingle.svc.handler.SvcResponseHandler;
import io.github.minguanq.mingle.svc.utils.DateUtils;
import io.github.minguanq.mingle.svc.utils.JacksonUtils;
import io.github.minguanq.mingle.svc.utils.SvcResUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * Filter for service pre-process and post-process,order must first before all filter
 *
 * @author Ming
 */

public class SvcProcessFilter extends AbstractSvcFilter {

    private final SvcMsgHandler svcMsgHandler;
    private final SvcProperties svcProperties;
    private final SvcLogHandler svcLogHandler;
    private final ExceptionHandlerResolver exceptionHandlerResolver;
    private final SvcBinderComponent svcBinderComponent;
    private final JacksonUtils jacksonUtils;
    private final SvcResUtils svcResUtils;

    public SvcProcessFilter(SvcInfo svcInfo, SvcMsgHandler svcMsgHandler, SvcProperties svcProperties, SvcLogHandler svcLogHandler, ExceptionHandlerResolver exceptionHandlerResolver, SvcBinderComponent svcBinderComponent, JacksonUtils jacksonUtils, SvcResUtils svcResUtils) {
        super(svcInfo);
        this.svcMsgHandler = svcMsgHandler;
        this.svcProperties = svcProperties;
        this.svcLogHandler = svcLogHandler;
        this.exceptionHandlerResolver = exceptionHandlerResolver;
        this.svcBinderComponent = svcBinderComponent;
        this.jacksonUtils = jacksonUtils;
        this.svcResUtils = svcResUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        try {
            ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
            ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
            svcInfo.setHttpServletRequest(requestWrapper);
            svcInfo.setHttpServletResponse(responseWrapper);
            start();
            filterChain.doFilter(svcInfo.getHttpServletRequest(), svcInfo.getHttpServletResponse());
            processResponseBody(responseWrapper);
        } catch (Exception e) {
            processExceptionBody(e, (ContentCachingResponseWrapper) svcInfo.getHttpServletResponse());
        } finally {
            SvcThreadLocal.remove();
        }
        end((ContentCachingResponseWrapper) svcInfo.getHttpServletResponse());
        if (svcInfo.getSvcBinderModel() != null) {
            if (svcInfo.getSvcBinderModel().getSvc().logging()) {
                convertLogMsg();
                svcLogHandler.writeEndLog(svcInfo);
            }
        }
    }

    private void start() {
        svcInfo.setStartDateTime(DateUtils.getNowLocalDateTime());
        svcInfo.setSvcResponseHeader(SvcResponseHeader.builder(svcProperties.getSuccessCode()).msg(svcProperties.getSuccessMsg()).build());
        Optional<SvcBinderComponent.SvcBinderModel> optionalSvcBinderModel = svcBinderComponent.getSvcBinderModel(svcInfo.getHttpServletRequest());
        if (optionalSvcBinderModel.isEmpty()) {
            throw new SvcNotFoundException("Can't found Svc in SvcBinderModel");
        }
        SvcBinderComponent.SvcBinderModel svcBinderModel = optionalSvcBinderModel.get();
        svcInfo.setSvcBinderModel(svcBinderModel);
    }

    private void end(ContentCachingResponseWrapper responseWrapper) throws IOException {
        svcInfo.setEndDateTime(DateUtils.getNowLocalDateTime());
        SvcResponseHandler svcResponseHandler = svcResUtils.build(svcInfo.getSvcResponseHeader().getCode(), convertMsg(svcInfo.getSvcResponseHeader()), svcInfo.getResponseBody());
        ObjectMapper objectMapper = jacksonUtils.getObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(objectMapper.writeValueAsString(svcResponseHandler));
        svcInfo.setSvcResponseHandler(svcResponseHandler);
        writeResponse(responseWrapper, jsonNode);
    }

    private void processResponseBody(ContentCachingResponseWrapper responseWrapper) throws JsonProcessingException {
        ObjectMapper objectMapper = jacksonUtils.getObjectMapper();
        svcInfo.setResponseBody(objectMapper.readTree(new String(responseWrapper.getContentAsByteArray(), StandardCharsets.UTF_8)));
    }

    private void processExceptionBody(Exception e, ContentCachingResponseWrapper responseWrapper) throws IOException {
        Exception exception = e;
        if (e instanceof ServletException servletException) {
            exception = (Exception) servletException.getRootCause();
        }
        ResponseEntity<SvcResponse> resolver = exceptionHandlerResolver.resolver(exception);
        ObjectMapper objectMapper = jacksonUtils.getObjectMapper();
        responseWrapper.setStatus(resolver.getStatusCode().value());
        resolver.getHeaders().forEach((s, strings) -> strings.forEach(node -> responseWrapper.setHeader(s, node)));
        svcInfo.setResponseBody(objectMapper.readTree(objectMapper.writeValueAsString(resolver.getBody())));
    }

    private void writeResponse(ContentCachingResponseWrapper responseWrapper, JsonNode jsonNode) throws IOException {
        responseWrapper.resetBuffer();
        responseWrapper.setContentType("application/json");
        responseWrapper.setCharacterEncoding("UTF-8");
        responseWrapper.getWriter().write(jsonNode.toString());
        responseWrapper.copyBodyToResponse();
    }

    private void convertLogMsg() {
        if(svcInfo.getSvcResponseHeaderLog() != null) {
            if (svcInfo.getSvcResponseHeaderLog().getCode() != null) {
                svcInfo.getSvcResponseHeaderLog().setMsg(convertMsg(svcInfo.getSvcResponseHeaderLog()));
            }
        }
    }

    private String convertMsg(SvcResponseHeader svcResponseHeader) {
        if (svcResponseHeader.getMsg() != null) {
            if (svcResponseHeader.getConvertMap() != null) {
                return svcMsgHandler.getMsg(svcProperties.getMsgType(), svcResponseHeader.getCode(), svcResponseHeader.getConvertMap());
            }
        } else {
            return svcMsgHandler.getMsg(svcProperties.getMsgType(), svcResponseHeader.getCode());
        }
        return svcResponseHeader.getMsg();
    }

}
