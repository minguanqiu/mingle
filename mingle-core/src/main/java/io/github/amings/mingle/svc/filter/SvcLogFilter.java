package io.github.amings.mingle.svc.filter;

import io.github.amings.mingle.svc.handler.SvcLogHandler;
import io.github.amings.mingle.svc.handler.model.SvcBeginModel;
import io.github.amings.mingle.utils.JacksonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Logging filter
 *
 * @author Ming
 */

public class SvcLogFilter extends AbstractSvcFilter {

    @Autowired
    private SvcLogHandler svcLogHandler;

    @Autowired
    @Qualifier("svcLogJacksonUtils")
    private JacksonUtils jacksonUtils;

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
            try {
                svcLogHandler.writeBeginLog(buildSvcBeginModel());
            } catch (Exception ignored) {

            }
        }
    }

    private void writeSvcBeginBack() {
        if (svcInfo.getSvcBinderModel() != null && svcInfo.getSvcBinderModel().isReqCustom()) {
            try {
                svcLogHandler.writeBeginLog(buildSvcBeginBackModel());
            } catch (Exception ignored) {

            }
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
