package io.github.amings.mingle.svc.filter;

import io.github.amings.mingle.svc.configuration.properties.SvcProperties;
import io.github.amings.mingle.svc.handler.SvcLogHandler;
import io.github.amings.mingle.svc.handler.SvcMsgHandler;
import io.github.amings.mingle.svc.handler.model.SvcBeginModel;
import io.github.amings.mingle.utils.JacksonUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Logging filter
 *
 * @author Ming
 */

public class SvcLogFilter extends AbstractSvcFilter {

    private final SvcLogHandler svcLogHandler;
    private final JacksonUtils jacksonUtils;

    public SvcLogFilter(SvcInfo svcInfo, SvcMsgHandler svcMsgHandler, SvcProperties svcProperties, SvcLogHandler svcLogHandler, JacksonUtils jacksonUtils) {
        super(svcInfo, svcMsgHandler, svcProperties);
        this.svcLogHandler = svcLogHandler;
        this.jacksonUtils = jacksonUtils;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        writeSvcBegin();
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            writeSvcBeginBack();
            throw e;
        }
        writeSvcBeginBack();
    }

    private void writeSvcBegin() {
        if (svcInfo.getSvcBinderModel() != null && !svcInfo.getSvcBinderModel().isReqCustom()) {
            svcInfo.setWriteBegin(true);
            svcLogHandler.writeBeginLog(buildSvcBeginModel());
        }
    }

    private void writeSvcBeginBack() {
        if (svcInfo.getSvcBinderModel() != null && svcInfo.getSvcBinderModel().isReqCustom()) {
            svcInfo.setWriteBegin(true);
            svcLogHandler.writeBeginLog(buildSvcBeginBackModel());
        }
    }

    private SvcBeginModel buildSvcBeginModel() {
        SvcBeginModel model = new SvcBeginModel();
        model.setHttpServletRequest(svcInfo.getHttpServletRequest());
        model.setUuid(svcInfo.getUuid());
        model.setName(svcInfo.getSvcName());
        model.setStartDateTime(svcInfo.getStartDateTime());
        jacksonUtils.readTree(svcInfo.getValidReqModel()).ifPresent(node -> {
            model.setModelBody(node.toString());
        });
        model.setPayloadBody(svcInfo.getPayLoadNode().toString());
        model.setIp(svcInfo.getIp());
        if (svcInfo.getSvcReqModelValidFailException() == null) {
            model.setValid(true);
        }
        return model;
    }

    private SvcBeginModel buildSvcBeginBackModel() {
        SvcBeginModel model = new SvcBeginModel();
        model.setHttpServletRequest(svcInfo.getHttpServletRequest());
        model.setUuid(svcInfo.getUuid());
        model.setName(svcInfo.getSvcName());
        model.setStartDateTime(svcInfo.getStartDateTime());
        jacksonUtils.readTree(svcInfo.getBackReqModel()).ifPresent(node -> {
            model.setModelBody(node.toString());
            model.setPayloadBody(node.toString());
        });
        model.setIp(svcInfo.getIp());
        if (svcInfo.getSvcReqModelValidFailException() == null) {
            model.setValid(true);
        }
        model.setBack(true);
        return model;
    }

}
