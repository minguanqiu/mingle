package io.github.minguanqiu.mingle.svc.session.utils;

import io.github.minguanqiu.mingle.svc.redis.RedisKey;
import io.github.minguanqiu.mingle.svc.session.SessionHeader;
import io.github.minguanqiu.mingle.svc.session.SessionInfo;
import io.github.minguanqiu.mingle.svc.session.dao.SessionDao;
import io.github.minguanqiu.mingle.svc.session.dao.entity.SessionEntity;
import io.github.minguanqiu.mingle.svc.session.exception.SessionTokenEncryptionErrorException;
import io.github.minguanqiu.mingle.svc.session.handler.SessionTokenHandler;
import io.github.minguanqiu.mingle.svc.utils.JacksonUtils;
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
    private final JacksonUtils jacksonUtils;

    public SessionUtils(SessionDao sessionDao, SessionTokenHandler sessionTokenHandler, JacksonUtils jacksonUtils) {
        this.sessionDao = sessionDao;
        this.sessionTokenHandler = sessionTokenHandler;
        this.jacksonUtils = jacksonUtils;
    }

    public String createSessionToken(RedisKey redisKey, SessionEntity session) {
        String encryption;
        try {
            SessionHeader sessionHeader = new SessionHeader(redisKey, String.valueOf(session.getTimeToLive()));
            encryption = sessionTokenHandler.encryption(jacksonUtils.readTree(sessionHeader).get().toString());
        } catch (Exception e) {
            throw new SessionTokenEncryptionErrorException(e);
        }
        sessionDao.save(session);
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
        sessionDao.save(getCurrentSession().session());
    }

    public void cleanSession() {
        sessionDao.delete(getCurrentSession().session());
    }

    public SessionInfo getCurrentSession() {
        return (SessionInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
