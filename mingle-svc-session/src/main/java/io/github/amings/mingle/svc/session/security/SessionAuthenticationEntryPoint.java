package io.github.amings.mingle.svc.session.security;

import io.github.amings.mingle.svc.session.exception.SvcAuthenticationFailException;
import io.github.amings.mingle.svc.session.utils.SessionCodeFiled;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * Custom spring security authenticationEntryPoint
 *
 * @author Ming
 */

@Slf4j
@Component
public class SessionAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        if(authException.getClass().equals(SvcAuthenticationFailException.class)) {
            throw authException;
        }
        log.error(authException.getMessage());
        throw new SvcAuthenticationFailException(SessionCodeFiled.MGS01, authException);
    }

}
