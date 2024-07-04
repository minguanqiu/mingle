package io.github.minguanqiu.mingle.svc.session.security;

import io.github.minguanqiu.mingle.svc.session.exception.SessionAccessDeniedException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * Custom spring security {@link AccessDeniedHandler}.
 *
 * @author Qiu Guan Ming
 */
public class SessionAccessDeniedHandler implements AccessDeniedHandler {

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException, ServletException {
    throw new SessionAccessDeniedException(accessDeniedException.getMessage(),
        accessDeniedException);
  }

}
