package io.github.minguanqiu.mingle.svc.session.security;

import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * {@link AbstractAuthenticationToken} for session.
 *
 * @author Qiu Guan Ming
 */

public class SessionAuthentication extends AbstractAuthenticationToken {

  private final Object principal;

  private final Object credentials;

  /**
   * Create a new SessionAuthentication instance.
   *
   * @param principal   the principal.
   * @param credentials the credentials.
   * @param authorities the authorities.
   */
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
