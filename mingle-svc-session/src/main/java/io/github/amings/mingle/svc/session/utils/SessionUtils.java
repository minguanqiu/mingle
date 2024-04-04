package io.github.amings.mingle.svc.session.utils;

import io.github.amings.mingle.svc.redis.RedisKey;
import io.github.amings.mingle.svc.session.SessionInfo;
import io.github.amings.mingle.svc.session.dao.SessionDao;
import io.github.amings.mingle.svc.session.dao.entity.Session;
import io.github.amings.mingle.svc.session.exception.SessionTokenEncryptionErrorException;
import io.github.amings.mingle.svc.session.handler.SessionTokenHandler;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * Utils for session feature
 *
 * @author Ming
 */
public class SessionUtils {

    private final SessionDao sessionDao;
    private final SessionTokenHandler sessionTokenHandler;

    public SessionUtils(SessionDao sessionDao, SessionTokenHandler sessionTokenHandler) {
        this.sessionDao = sessionDao;
        this.sessionTokenHandler = sessionTokenHandler;
    }

    public String generateToken(RedisKey redisKey, Session session) {
        String encryption;
        try {
            encryption = sessionTokenHandler.encryption(redisKey.toString());
        } catch (Exception e) {
            throw new SessionTokenEncryptionErrorException("Session token encryption error");
        }
        sessionDao.set(redisKey, session, session.getTimeToLive());
        return encryption;
    }


    @SuppressWarnings("unchecked")
    public <T> Optional<T> getSessionValue(String name) {
        return (Optional<T>) Optional.ofNullable(getCurrentSession().session().getSessionValue().get(name));
    }

    public void setSessionValue(String key, Object value) {
        getCurrentSession().session().getSessionValue().put(key, value);
    }

    public void removeSessionValue(String key) {
        getCurrentSession().session().getSessionValue().remove(key);
    }

    private void updateSession() {
        sessionDao.set(getCurrentSession().sessionHeader().redisKey(), getCurrentSession().session(), getCurrentSession().session().getTimeToLive());
    }

    public void cleanSession() {
        sessionDao.del(getCurrentSession().sessionHeader().redisKey());
    }

    public SessionInfo getCurrentSession() {
        return (SessionInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
