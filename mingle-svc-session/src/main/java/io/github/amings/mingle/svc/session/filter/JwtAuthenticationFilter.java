package io.github.amings.mingle.svc.session.filter;

import io.github.amings.mingle.svc.config.properties.SvcProperties;
import io.github.amings.mingle.svc.filter.AbstractSvcFilter;
import io.github.amings.mingle.svc.filter.SvcInfo;
import io.github.amings.mingle.svc.handler.SvcMsgHandler;
import io.github.amings.mingle.svc.session.exception.JwtDecryptionFailException;
import io.github.amings.mingle.svc.session.exception.JwtHeaderMissingException;
import io.github.amings.mingle.svc.session.exception.SessionInfoDeserializeFailException;
import io.github.amings.mingle.svc.session.security.SessionAuthentication;
import io.github.amings.mingle.svc.session.security.model.SessionInfo;
import io.github.amings.mingle.svc.session.utils.SessionUtils;
import io.github.amings.mingle.utils.JacksonUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Optional;

/**
 * Jwt authentication valid filter
 *
 * @author Ming
 */

public class JwtAuthenticationFilter extends AbstractSvcFilter {

    private final SessionUtils sessionUtils;
    private final JacksonUtils jacksonUtils;

    public JwtAuthenticationFilter(SvcInfo svcInfo, SvcMsgHandler svcMsgHandler, SvcProperties svcProperties, SessionUtils sessionUtils, JacksonUtils jacksonUtils) {
        super(svcInfo, svcMsgHandler, svcProperties);
        this.sessionUtils = sessionUtils;
        this.jacksonUtils = jacksonUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jweHeader = request.getHeader("Authorization");
        if (jweHeader == null) {
            throw new JwtHeaderMissingException("JWT header missing");
        }
        Optional<String> jweOptional = sessionUtils.decryptionJWEToken(jweHeader.replace("Bearer ", ""));
        if (jweOptional.isEmpty()) {
            throw new JwtDecryptionFailException("JWT decryption fail");
        }
        String jwe = jweOptional.get();
        Optional<SessionInfo> sessionInfoOptional = jacksonUtils.readValue(jwe, SessionInfo.class);
        if (sessionInfoOptional.isEmpty()) {
            throw new SessionInfoDeserializeFailException("SessionInfo deserialize fail");
        }
        SessionAuthentication sessionAuthentication = new SessionAuthentication(null, sessionInfoOptional.get());
        sessionAuthentication.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(sessionAuthentication);
        filterChain.doFilter(request, response);
    }
}
