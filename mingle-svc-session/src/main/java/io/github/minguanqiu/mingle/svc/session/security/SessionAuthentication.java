package io.github.minguanqiu.mingle.svc.session.security;

import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * {@link AbstractAuthenticationToken} for session
 *
 * @author Qiu Guan Ming
 */

public class SessionAuthentication extends AbstractAuthenticationToken {

  private final Object principal;

  private final Object credentials;

  public SessionAuthentication(Object principal, Object credentials,
      Collection<? extends GrantedAuthority> authorities) {
    super(authorities);
    this.principal = principal;
    this.credentials = credentials;
  }

  @Override
  public Object getCredentials() {
    return credentials;
  }

  @Override
  public Object getPrincipal() {
    return principal;
  }
}
