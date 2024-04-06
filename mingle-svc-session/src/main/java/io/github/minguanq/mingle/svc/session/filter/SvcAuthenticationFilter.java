package io.github.minguanq.mingle.svc.session.filter;

import io.github.minguanq.mingle.svc.component.SvcRegisterComponent;
import io.github.minguanq.mingle.svc.redis.RedisKey;
import io.github.minguanq.mingle.svc.session.annotation.SvcSession;
import io.github.minguanq.mingle.svc.session.configuration.properties.SessionProperties;
import io.github.minguanq.mingle.svc.session.dao.SessionDao;
import io.github.minguanq.mingle.svc.session.dao.entity.Session;
import io.github.minguanq.mingle.svc.session.exception.SessionHeaderMissingException;
import io.github.minguanq.mingle.svc.session.exception.SessionNotExistException;
import io.github.minguanq.mingle.svc.session.exception.SessionTokenDecryptionErrorException;
import io.github.minguanq.mingle.svc.session.exception.SessionTypeIncorrectException;
import io.github.minguanq.mingle.svc.session.handler.SessionTokenHandler;
import io.github.minguanq.mingle.svc.session.security.SessionAuthentication;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Check and decryption token after create authentication
 *
 * @author Ming
 */
public class SvcAuthenticationFilter extends OncePerRequestFilter {
    private final SvcRegisterComponent svcRegisterComponent;
    private final SessionProperties sessionProperties;
    private final SessionDao sessionDao;
    private final SessionTokenHandler sessionTokenHandler;

    public SvcAuthenticationFilter(SvcRegisterComponent svcRegisterComponent, SessionProperties sessionProperties, SessionDao sessionDao, SessionTokenHandler sessionTokenHandler) {
        this.svcRegisterComponent = svcRegisterComponent;
        this.sessionProperties = sessionProperties;
        this.sessionDao = sessionDao;
        this.sessionTokenHandler = sessionTokenHandler;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String cipherText = request.getHeader(sessionProperties.getHeader());
        if (cipherText == null) {
            throw new SessionHeaderMissingException("session header missing");
        }
        String plainText;
        try {
            plainText = sessionTokenHandler.decryption(cipherText);
        } catch (Exception e) {
            throw new SessionTokenDecryptionErrorException("Session token decryption error");
        }
        RedisKey redisKey = new RedisKey(List.of(plainText.split(RedisKey.KEY_DELIMITER)));
        Session session = sessionDao.get(redisKey).orElseThrow(() -> new SessionNotExistException("Session not exist"));
        String[] types = svcRegisterComponent.getSvcDefinition(request).get().getSvcClass().getAnnotation(SvcSession.class).type();
        if (!checkType(types, session.getType())) {
            throw new SessionTypeIncorrectException("Session type incorrect");
        }
        sessionDao.set(redisKey, session, session.getTimeToLive()); // refresh session with origin live time
        SessionAuthentication authentication = new SessionAuthentication(session, null, AuthorityUtils.createAuthorityList(session.getAuthorities().toArray(new String[0])));
        authentication.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private boolean checkType(String[] types, String sessionType) {
        boolean isPass = false;
        for (String type : types) {
            if (type.equals(sessionType)) {
                isPass = true;
                break;
            }
        }
        return isPass;
    }

}
