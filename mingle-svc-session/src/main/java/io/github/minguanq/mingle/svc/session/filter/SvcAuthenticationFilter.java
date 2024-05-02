package io.github.minguanq.mingle.svc.session.filter;

import io.github.minguanq.mingle.svc.register.SvcRegister;
import io.github.minguanq.mingle.svc.session.SessionHeader;
import io.github.minguanq.mingle.svc.session.configuration.properties.SvcSessionProperties;
import io.github.minguanq.mingle.svc.session.dao.SessionDao;
import io.github.minguanq.mingle.svc.session.dao.entity.SessionEntity;
import io.github.minguanq.mingle.svc.session.exception.*;
import io.github.minguanq.mingle.svc.session.handler.SessionTokenHandler;
import io.github.minguanq.mingle.svc.session.handler.model.SvcSessionFeature;
import io.github.minguanq.mingle.svc.session.security.SessionAuthentication;
import io.github.minguanq.mingle.svc.utils.JacksonUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Check and decryption token after create authentication
 *
 * @author Ming
 */
public class SvcAuthenticationFilter extends OncePerRequestFilter {
    private final SvcRegister svcRegister;
    private final SvcSessionProperties svcSessionProperties;
    private final SessionDao sessionDao;
    private final SessionTokenHandler sessionTokenHandler;
    private final JacksonUtils jacksonUtils;

    public SvcAuthenticationFilter(SvcRegister svcRegister, SvcSessionProperties svcSessionProperties, SessionDao sessionDao, SessionTokenHandler sessionTokenHandler, JacksonUtils jacksonUtils) {
        this.svcRegister = svcRegister;
        this.svcSessionProperties = svcSessionProperties;
        this.sessionDao = sessionDao;
        this.sessionTokenHandler = sessionTokenHandler;
        this.jacksonUtils = jacksonUtils;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String cipherText = request.getHeader(svcSessionProperties.getHeader());
        if (cipherText == null) {
            throw new SessionHeaderMissingException();
        }
        String plainText;
        try {
            plainText = sessionTokenHandler.decryption(cipherText);
        } catch (Exception e) {
            throw new SessionTokenDecryptionErrorException();
        }
        SessionHeader sessionHeader = jacksonUtils.readValue(plainText, SessionHeader.class).orElseThrow(SessionHeaderDeserializeErrorException::new);
        SessionEntity session = sessionDao.findById(sessionHeader.redisKey()).orElseThrow(SessionNotExistException::new);
        session.setTimeToLive(Long.parseLong(sessionHeader.refreshTime()));
        SvcRegister.SvcDefinition svcDefinition = svcRegister.getSvcDefinition(request).get();
        SvcSessionFeature svcSessionFeature = svcDefinition.getFeature(SvcSessionFeature.class).get();
        String[] types = svcSessionFeature.types();
        if (!checkType(types, session.getType())) {
            throw new SessionTypeIncorrectException();
        }
        sessionDao.save(session); // refresh session with origin live time
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
