package io.github.minguanqiu.mingle.svc.filter;

import io.github.minguanqiu.mingle.svc.exception.IPAuthenticationFailException;
import io.github.minguanqiu.mingle.svc.handler.model.SvcFeature;
import io.github.minguanqiu.mingle.svc.register.SvcDefinition;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filter for service ip address secure
 *
 * @author Qiu Guan Ming
 */
public class SvcIpSecureFilter extends AbstractSvcFilter {

  /**
   * Create a new SvcIpSecureFilter instance.
   *
   * @param svcInfo the service information.
   */
  public SvcIpSecureFilter(SvcInfo svcInfo) {
    super(svcInfo);
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    checkSecure();
    filterChain.doFilter(request, response);
  }

  /**
   * Check ip address secure.
   */
  private void checkSecure() {
    SvcDefinition svcDefinition = svcInfo.getSvcDefinition();
    if (!checkIpAddress(svcDefinition.getFeature(SvcFeature.class).get().ip_secure(),
        svcInfo.getHttpServletRequest().getRemoteAddr())) {
      throw new IPAuthenticationFailException();
    }
  }

  /**
   * Check ip address contains.
   *
   * @param ips        the ip address array.
   * @param comeFromIp the target ip address.
   * @return return ture or false if contains.
   */
  private boolean checkIpAddress(String[] ips, String comeFromIp) {
    for (String ip : ips) {
      if (ip.equals(comeFromIp)) {
        return true;
      }
    }
    return false;
  }

}
