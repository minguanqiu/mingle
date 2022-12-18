package io.github.amings.mingle.svc.session.filter;

import io.github.amings.mingle.svc.filter.AbstractSvcFilter;
import io.github.amings.mingle.svc.session.exception.JwtDecryptionFailException;
import io.github.amings.mingle.svc.session.exception.JwtHeaderMissingException;
import io.github.amings.mingle.svc.session.exception.SessionInfoDeserializeFailException;
import io.github.amings.mingle.svc.session.security.SessionAuthentication;
import io.github.amings.mingle.svc.session.security.model.SessionInfo;
import io.github.amings.mingle.svc.session.utils.JwtUtils;
import io.github.amings.mingle.svc.session.utils.SessionUtils;
import io.github.amings.mingle.utils.JacksonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * Jwt authentication valid filter
 *
 * @author Ming
 */

public class JwtAuthenticationFilter extends AbstractSvcFilter {
    @Autowired
    SessionUtils sessionUtils;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    JacksonUtils jacksonUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jweHeader = request.getHeader("Authorization");
        if (jweHeader == null) {
            throw new JwtHeaderMissingException("JWT header missing");
        }
        Optional<String> jweOptional = sessionUtils.decryptionJWEToken(jweHeader.replace("Bearer ", ""));
        if (!jweOptional.isPresent()) {
            throw new JwtDecryptionFailException("JWT decryption fail");
        }
        String jwe = jweOptional.get();
        Optional<SessionInfo> sessionInfoOptional = jacksonUtils.readValue(jwe, SessionInfo.class);
        if (!sessionInfoOptional.isPresent()) {
            throw new SessionInfoDeserializeFailException("SessionInfo deserialize fail");
        }
        SecurityContextHolder.getContext().setAuthentication(new SessionAuthentication(null, sessionInfoOptional.get()));
        filterChain.doFilter(request, response);
    }
}
