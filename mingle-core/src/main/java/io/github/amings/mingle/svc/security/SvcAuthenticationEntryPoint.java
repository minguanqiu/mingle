package io.github.amings.mingle.svc.security;

import io.github.amings.mingle.svc.exception.SvcAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Custom Spring Security AccessDeniedHandler
 *
 * @author Ming
 */

@Component
public class SvcAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        throw new SvcAuthenticationException(authException.getMessage(), authException);
    }
}
