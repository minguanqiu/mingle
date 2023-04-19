package io.github.amings.mingle.svc.session.security;

import io.github.amings.mingle.svc.redis.Redis0;
import io.github.amings.mingle.svc.redis.RedisKey;
import io.github.amings.mingle.svc.session.component.SessionBinderComponent;
import io.github.amings.mingle.svc.session.dao.SessionDao;
import io.github.amings.mingle.svc.session.dao.entity.SessionEntity;
import io.github.amings.mingle.svc.session.exception.SessionKickException;
import io.github.amings.mingle.svc.session.exception.SessionNotFoundException;
import io.github.amings.mingle.svc.session.exception.SessionTypeIncorrectException;
import io.github.amings.mingle.svc.session.security.model.SessionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.Optional;

/**
 * Session authentication provider
 *
 * @author Ming
 */

@Component
public class SessionAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    SessionBinderComponent sessionBinderComponent;
    @Autowired
    HttpServletRequest httpServletRequest;
    @Autowired
    SessionDao sessionDao;
    @Autowired
    Redis0 redis0;
    @Autowired
    Session session;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SessionInfo sessionInfo = (SessionInfo) authentication.getCredentials();
        RedisKey redisKey = RedisKey.of(sessionInfo.getKey());
        String type = sessionBinderComponent.getSessionMap().get(httpServletRequest.getServletPath());
        if (!type.equals("refresh")) {
            if (!type.equals(sessionInfo.getType())) {
                throw new SessionTypeIncorrectException("Session type incorrect");
            }
        }
        Optional<SessionEntity> sessionEntityOptional = sessionDao.get(RedisKey.of(sessionInfo.getKey()));
        if (!sessionEntityOptional.isPresent()) {
            throw new SessionNotFoundException("Session not found");
        }
        SessionEntity sessionEntity = sessionEntityOptional.get();
        if (sessionInfo.isSingle()) { // if single enable
            if (!sessionEntity.getId().equals(sessionInfo.getId())) { // check two sessionId and kick
                throw new SessionKickException("Session has been logout by another session");
            }
        }
        sessionDao.set(redisKey, sessionEntity, Duration.parse(sessionInfo.getTimeToLive())); // refresh session live time
        session.setSessionInfo(sessionInfo);
        session.setSessionEntity(sessionEntity);
        return new SessionAuthentication(sessionEntity, sessionInfo, AuthorityUtils.createAuthorityList(sessionEntity.getAuthorities().toArray(new String[0])));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(SessionAuthentication.class);
    }

}
