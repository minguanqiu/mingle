package io.github.minguanqiu.mingle.svc.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.minguanqiu.mingle.svc.SvcResponseBody;
import io.github.minguanqiu.mingle.svc.SvcResponseHeader;
import io.github.minguanqiu.mingle.svc.concurrent.SvcThreadLocal;
import io.github.minguanqiu.mingle.svc.configuration.properties.SvcProperties;
import io.github.minguanqiu.mingle.svc.exception.SvcNotFoundException;
import io.github.minguanqiu.mingle.svc.exception.handler.resolver.ExceptionHandlerResolver;
import io.github.minguanqiu.mingle.svc.handler.CodeMessageHandler;
import io.github.minguanqiu.mingle.svc.handler.SvcLoggingHandler;
import io.github.minguanqiu.mingle.svc.handler.SvcResponseHandler;
import io.github.minguanqiu.mingle.svc.register.SvcDefinition;
import io.github.minguanqiu.mingle.svc.register.SvcRegister;
import io.github.minguanqiu.mingle.svc.utils.DateUtils;
import io.github.minguanqiu.mingle.svc.utils.JacksonUtils;
import io.github.minguanqiu.mingle.svc.utils.StringUtils;
import io.github.minguanqiu.mingle.svc.utils.SvcResUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

/**
 * Filter for service pre-process and post-process,order must first before all filter
 *
 * @author Qiu Guan Ming
 */

public class SvcProcessFilter extends AbstractSvcFilter {

  private final CodeMessageHandler codeMessageHandler;
  private final SvcProperties svcProperties;
  private final SvcLoggingHandler svcLoggingHandler;
  private final ExceptionHandlerResolver exceptionHandlerResolver;
  private final SvcRegister svcRegister;
  private final JacksonUtils jacksonUtils;
  private final SvcResUtils svcResUtils;

  /**
   * Create a new SvcProcessFilter instance.
   *
   * @param svcInfo                  the service information.
   * @param codeMessageHandler       the code message handler.
   * @param svcProperties            the service properties.
   * @param svcLoggingHandler        the service logging handler.
   * @param exceptionHandlerResolver the exception handler resolver.
   * @param svcRegister              the service register.
   * @param jacksonUtils             the jackson utils.
   * @param svcResUtils              the service response utils.
   */
  public SvcProcessFilter(SvcInfo svcInfo, CodeMessageHandler codeMessageHandler,
      SvcProperties svcProperties, SvcLoggingHandler svcLoggingHandler,
      ExceptionHandlerResolver exceptionHandlerResolver, SvcRegister svcRegister,
      JacksonUtils jacksonUtils, SvcResUtils svcResUtils) {
    super(svcInfo);
    this.codeMessageHandler = codeMessageHandler;
    this.svcProperties = svcProperties;
    this.svcLoggingHandler = svcLoggingHandler;
    this.exceptionHandlerResolver = exceptionHandlerResolver;
    this.svcRegister = svcRegister;
    this.jacksonUtils = jacksonUtils;
    this.svcResUtils = svcResUtils;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws IOException {
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
    if (svcInfo.getSvcSerialNum() != null) {
      svcLoggingHandler.writeEndLog(svcInfo);
    }
  }

  /**
   * Pre-processing request for service.
   */
  private void start() {
    svcInfo.setStartDateTime(DateUtils.getNowLocalDateTime());
    svcInfo.setSvcResponseHeader(
        SvcResponseHeader.builder(svcProperties.getCode()).msg(svcProperties.getMsg()).build());
    Optional<SvcDefinition> optionalSvcBinderModel = svcRegister.getSvcDefinition(
        svcInfo.getHttpServletRequest());
    if (optionalSvcBinderModel.isEmpty()) {
      throw new SvcNotFoundException();
    }
    SvcDefinition svcDefinition = optionalSvcBinderModel.get();
    svcInfo.setSvcDefinition(svcDefinition);
  }

  /**
   * Post-processing response for service.
   */
  private void end(ContentCachingResponseWrapper responseWrapper) throws IOException {
    svcInfo.setEndDateTime(DateUtils.getNowLocalDateTime());
    SvcResponseHandler svcResponseHandler = svcResUtils.build(
        svcInfo.getSvcResponseHeader().getCode(), convertMsg(svcInfo.getSvcResponseHeader()),
        svcInfo.getResponseBody());
    ObjectMapper objectMapper = jacksonUtils.getObjectMapper();
    JsonNode jsonNode = objectMapper.readTree(objectMapper.writeValueAsString(svcResponseHandler));
    writeResponse(responseWrapper, jsonNode);
  }

  /**
   * Process response body.
   *
   * @param responseWrapper the response wrapper.
   * @throws JsonProcessingException when jackson resolve error.
   */
  private void processResponseBody(ContentCachingResponseWrapper responseWrapper)
      throws JsonProcessingException {
    ObjectMapper objectMapper = jacksonUtils.getObjectMapper();
    JsonNode node = objectMapper.readTree(
        new String(responseWrapper.getContentAsByteArray(), StandardCharsets.UTF_8));
    if (!node.isMissingNode()) {
      svcInfo.setResponseBody(node);
    }
  }

  /**
   * Process exception to resolver.
   *
   * @param e               the exception.
   * @param responseWrapper the response wrapper.
   * @throws IOException when jackson resolve error.
   */
  private void processExceptionBody(Exception e, ContentCachingResponseWrapper responseWrapper)
      throws JsonProcessingException {
    Exception exception = e;
    if (e instanceof ServletException servletException) {
      exception = (Exception) servletException.getRootCause();
    }
    ResponseEntity<SvcResponseBody> resolver = exceptionHandlerResolver.resolve(exception);
    ObjectMapper objectMapper = jacksonUtils.getObjectMapper();
    responseWrapper.setStatus(resolver.getStatusCode().value());
    resolver.getHeaders()
        .forEach((s, strings) -> strings.forEach(node -> responseWrapper.setHeader(s, node)));
    if (resolver.getBody() != null) {
      svcInfo.setResponseBody(
          objectMapper.readTree(objectMapper.writeValueAsString(resolver.getBody())));
    }
  }

  /**
   * Write data to http servlet response.
   *
   * @param responseWrapper the response wrapper.
   * @param jsonNode        the jackson node.
   * @throws IOException when http servlet response error.
   */
  private void writeResponse(ContentCachingResponseWrapper responseWrapper, JsonNode jsonNode)
      throws IOException {
    responseWrapper.resetBuffer();
    responseWrapper.setContentType("application/json");
    responseWrapper.setCharacterEncoding("UTF-8");
    responseWrapper.getWriter().write(jsonNode.toString());
    responseWrapper.copyBodyToResponse();
  }

  /**
   * Get response message from CodeMessageHandler.
   *
   * @param svcResponseHeader the service response header.
   * @return return the response message.
   */
  private String getMsg(SvcResponseHeader svcResponseHeader) {
    if (svcResponseHeader.getMsg() == null) {
      return codeMessageHandler.getMsg(svcProperties.getMsgType(), svcResponseHeader.getCode())
          .orElse(null);
    }
    return svcResponseHeader.getMsg();
  }

  /**
   * Check response message need convert.
   *
   * @param svcResponseHeader the service response header.
   * @return return the response message.
   */
  private String convertMsg(SvcResponseHeader svcResponseHeader) {
    String msg = getMsg(svcResponseHeader);
    if (msg != null && svcResponseHeader.getConvertMap() != null) {
      return StringUtils.templateConvert(msg, svcResponseHeader.getConvertMap(), "{", "}");
    }
    return msg;
  }

}
