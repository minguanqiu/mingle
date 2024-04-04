package io.github.minguanq.mingle.svc.session.security;

import io.github.minguanq.mingle.svc.session.exception.SessionAccessDeniedException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

/**
 * Custom spring security {@link AccessDeniedHandler}
 *
 * @author Ming
 */
public class SessionAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        throw new SessionAccessDeniedException(accessDeniedException.getMessage(), accessDeniedException);
    }

}
