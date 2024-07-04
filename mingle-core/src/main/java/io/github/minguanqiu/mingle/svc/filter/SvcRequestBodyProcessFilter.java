package io.github.minguanqiu.mingle.svc.filter;

import io.github.minguanqiu.mingle.svc.exception.ReqBodyNotJsonFormatException;
import io.github.minguanqiu.mingle.svc.handler.SvcRequestBodyProcessHandler;
import io.github.minguanqiu.mingle.svc.handler.model.SvcFeature;
import io.github.minguanqiu.mingle.svc.utils.JacksonUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import org.springframework.web.util.ContentCachingRequestWrapper;

/**
 * Filter for service request body process
 *
 * @author Qiu Guan Ming
 */
public class SvcRequestBodyProcessFilter extends AbstractSvcFilter {

  private final SvcRequestBodyProcessHandler svcRequestBodyProcessHandler;

  private final JacksonUtils jacksonUtils;

  /**
   * Create a new SvcRequestBodyProcessFilter instance.
   *
   * @param svcInfo                      the service information.
   * @param svcRequestBodyProcessHandler the service request body process handler.
   * @param jacksonUtils                 the jackson utils.
   */
  public SvcRequestBodyProcessFilter(SvcInfo svcInfo,
      SvcRequestBodyProcessHandler svcRequestBodyProcessHandler, JacksonUtils jacksonUtils) {
    super(svcInfo);
    this.svcRequestBodyProcessHandler = svcRequestBodyProcessHandler;
    this.jacksonUtils = jacksonUtils;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    processRequestBody();
    filterChain.doFilter(request, response);
  }

  /**
   * Pre-processing request body.
   *
   * @throws IOException when process input stream error.
   */
  private void processRequestBody() throws IOException {
    String body = getBody((ContentCachingRequestWrapper) svcInfo.getHttpServletRequest());
    String payLoadBody =
        svcInfo.getSvcDefinition().getFeature(SvcFeature.class).get().body_process()
            ? svcRequestBodyProcessHandler.processBody(body) : body;
    boolean isJson = jacksonUtils.isJson(payLoadBody);
    if (!isJson) {
      throw new ReqBodyNotJsonFormatException();
    }
    svcInfo.setRequestBody(payLoadBody);
  }

  /**
   * Get request body from request wrapper.
   *
   * @param reqWrapper the request wrapper.
   * @throws IOException when process input stream error.
   */
  private String getBody(ContentCachingRequestWrapper reqWrapper) throws IOException {
    String body;
    try (BufferedReader bufReader = new BufferedReader(
        new InputStreamReader(reqWrapper.getInputStream()))) {
      body = bufReader.lines().collect(Collectors.joining());
    }
    return body;
  }

}
