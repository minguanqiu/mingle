package io.github.minguanqiu.mingle.svc.session.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

/**
 * Session authentication provider
 *
 * @author Qiu Guan Ming
 */
public class SessionAuthenticationProvider implements AuthenticationProvider {

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    return authentication;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return false;
  }

}
