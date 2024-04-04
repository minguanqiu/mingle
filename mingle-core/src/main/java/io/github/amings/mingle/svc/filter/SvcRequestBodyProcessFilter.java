package io.github.amings.mingle.svc.filter;

import io.github.amings.mingle.svc.exception.ReqBodyNotJsonFormatException;
import io.github.amings.mingle.svc.handler.SvcRequestBodyProcessHandler;
import io.github.amings.mingle.svc.utils.JacksonUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

/**
 * Filter for service request body process
 *
 * @author Ming
 */
public class SvcRequestBodyProcessFilter extends AbstractSvcFilter {

    private final SvcRequestBodyProcessHandler svcRequestBodyProcessHandler;

    private final JacksonUtils jacksonUtils;

    public SvcRequestBodyProcessFilter(SvcInfo svcInfo, SvcRequestBodyProcessHandler svcRequestBodyProcessHandler, JacksonUtils jacksonUtils) {
        super(svcInfo);
        this.svcRequestBodyProcessHandler = svcRequestBodyProcessHandler;
        this.jacksonUtils = jacksonUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        processRequestBody();
        filterChain.doFilter(request, response);
    }

    private void processRequestBody() throws IOException {
        String body = getBody((ContentCachingRequestWrapper) svcInfo.getHttpServletRequest());
        String payLoadBody =  svcRequestBodyProcessHandler.processBody(body);
        boolean isJson = jacksonUtils.isJson(payLoadBody);
        if (!isJson) {
            throw new ReqBodyNotJsonFormatException("Request body not json format");
        }
        svcInfo.setPayLoadString(payLoadBody);
    }

    private String getBody(ContentCachingRequestWrapper reqWrapper) throws IOException {
        String body;
        try (BufferedReader bufReader = new BufferedReader(new InputStreamReader(reqWrapper.getInputStream()))) {
            body = bufReader.lines().collect(Collectors.joining());
        }
        return body;
    }

}
