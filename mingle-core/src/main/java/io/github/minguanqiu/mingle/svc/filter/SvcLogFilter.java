package io.github.minguanqiu.mingle.svc.filter;

import io.github.minguanqiu.mingle.svc.concurrent.Attribute;
import io.github.minguanqiu.mingle.svc.concurrent.SvcAttributeName;
import io.github.minguanqiu.mingle.svc.concurrent.SvcThreadLocal;
import io.github.minguanqiu.mingle.svc.handler.SerialNumberGeneratorHandler;
import io.github.minguanqiu.mingle.svc.handler.SvcLoggingHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter for service logging
 *
 * @author Qiu Guan Ming
 */

public class SvcLogFilter extends AbstractSvcFilter {

  private final SvcLoggingHandler svcLoggingHandler;
  private final SerialNumberGeneratorHandler serialNumberGeneratorHandler;

  public SvcLogFilter(SvcInfo svcInfo, SvcLoggingHandler svcLoggingHandler,
      SerialNumberGeneratorHandler serialNumberGeneratorHandler) {
    super(svcInfo);
    this.svcLoggingHandler = svcLoggingHandler;
    this.serialNumberGeneratorHandler = serialNumberGeneratorHandler;
  }


  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    svcInfo.setSvcSerialNum(serialNumberGeneratorHandler.generate("svc"));
    SvcThreadLocal.set(buildSvcLogModel());
    writeSvcBegin();
    filterChain.doFilter(request, response);
  }

  private void writeSvcBegin() {
    svcLoggingHandler.writeBeginLog(svcInfo);
  }

  private Attribute buildSvcLogModel() {
    Attribute attribute = new Attribute();
    attribute.setAttributes(SvcAttributeName.SVC_SERIAL_NUMBER, svcInfo.getSvcSerialNum());
    return attribute;
  }

}
