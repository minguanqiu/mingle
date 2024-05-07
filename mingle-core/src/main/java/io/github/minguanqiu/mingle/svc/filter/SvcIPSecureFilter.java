package io.github.minguanqiu.mingle.svc.filter;

import io.github.minguanqiu.mingle.svc.exception.IPAuthenticationFailException;
import io.github.minguanqiu.mingle.svc.handler.model.SvcFeature;
import io.github.minguanqiu.mingle.svc.register.SvcRegister;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Filter for service ip address secure
 *
 * @author Ming
 */
public class SvcIPSecureFilter extends AbstractSvcFilter {

    public SvcIPSecureFilter(SvcInfo svcInfo) {
        super(svcInfo);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        checkSecure();
        filterChain.doFilter(request, response);
    }

    private void checkSecure() {
        SvcRegister.SvcDefinition svcDefinition = svcInfo.getSvcDefinition();
        if (!checkIpAddress(svcDefinition.getFeature(SvcFeature.class).get().ip_secure(), svcInfo.getHttpServletRequest().getRemoteAddr())) {
            throw new IPAuthenticationFailException();
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

}
