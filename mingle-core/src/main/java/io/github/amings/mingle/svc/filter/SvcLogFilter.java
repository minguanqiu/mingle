package io.github.amings.mingle.svc.filter;

import io.github.amings.mingle.svc.SvcResponseHeader;
import io.github.amings.mingle.svc.concurrent.SvcAttribute;
import io.github.amings.mingle.svc.concurrent.SvcThreadLocal;
import io.github.amings.mingle.svc.handler.SerialNumberGeneratorHandler;
import io.github.amings.mingle.svc.handler.SvcLogHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Filter for service logging
 *
 * @author Ming
 */

public class SvcLogFilter extends AbstractSvcFilter {

    private final SvcLogHandler svcLogHandler;
    private final SerialNumberGeneratorHandler serialNumberGeneratorHandler;

    public SvcLogFilter(SvcInfo svcInfo, SvcLogHandler svcLogHandler, SerialNumberGeneratorHandler serialNumberGeneratorHandler) {
        super(svcInfo);
        this.svcLogHandler = svcLogHandler;
        this.serialNumberGeneratorHandler = serialNumberGeneratorHandler;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        svcInfo.setSvcSerialNum(serialNumberGeneratorHandler.generate("svc"));
        svcInfo.setSvcResponseHeaderLog(SvcResponseHeader.builder(null).build());
        SvcThreadLocal.set(buildSvcLogModel());
        writeSvcBegin();
        filterChain.doFilter(request, response);
    }

    private void writeSvcBegin() {
        svcLogHandler.writeBeginLog(svcInfo);
    }

    private SvcAttribute buildSvcLogModel() {
        SvcAttribute svcAttribute = new SvcAttribute();
        svcAttribute.setAttributes(SvcAttribute.Name.SVC_SERIAL_NUMBER, svcInfo.getSvcSerialNum());
        return svcAttribute;
    }

}
