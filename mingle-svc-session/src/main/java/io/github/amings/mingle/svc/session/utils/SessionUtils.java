package io.github.amings.mingle.svc.session.utils;

import com.nimbusds.jose.JOSEException;
import io.github.amings.mingle.svc.redis.RedisKey;
import io.github.amings.mingle.svc.session.dao.SessionDao;
import io.github.amings.mingle.svc.session.dao.entity.SessionEntity;
import io.github.amings.mingle.svc.session.security.Session;
import io.github.amings.mingle.svc.session.security.model.SessionInfo;
import io.github.amings.mingle.utils.JacksonUtils;
import io.github.amings.mingle.utils.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Control session utils
 *
 * @author Ming
 */

@Component
public class SessionUtils {

    @Autowired
    Session session;
    @Autowired
    SessionDao sessionDao;
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    JacksonUtils jacksonUtils;

    public String createSession(List<String> keyParams, String sessionType, Duration timeToLive) {
        return createSession(keyParams, sessionType, timeToLive, new HashMap<>());
    }

    public String createSession(List<String> keyParams, String sessionType, Duration timeToLive, Map<String, Object> sessionValue) {
        return createSession(keyParams, sessionType, timeToLive, sessionValue, new ArrayList<>());
    }

    public String createSession(List<String> keyParams, String sessionType, Duration timeToLive, Map<String, Object> sessionValue, ArrayList<String> authorities) {
        return createSession(keyParams, sessionType, timeToLive, sessionValue, authorities, false);
    }

    public String createSession(List<String> keyParams, String sessionType, Duration timeToLive, Map<String, Object> sessionValue, ArrayList<String> authorities, boolean single) {
        RedisKey redisKey = new RedisKey(SessionEntity.class);
        redisKey.addParams(keyParams);
        String sessionId = UUIDUtils.generateUuid();
        if (!single) {
            redisKey.addParam(sessionId);
        }
        SessionEntity sessionEntity = new SessionEntity();
        sessionEntity.setId(sessionId);
        sessionEntity.setType(sessionType);
        sessionEntity.setAuthorities(authorities);
        sessionEntity.setSessionValue(sessionValue);
        sessionDao.set(redisKey, sessionEntity, timeToLive);

        SessionInfo sessionInfo = new SessionInfo();
        sessionInfo.setKey(redisKey.format());
        sessionInfo.setId(sessionId);
        sessionInfo.setType(sessionType);
        sessionInfo.setTimeToLive(timeToLive.toString());
        sessionInfo.setSingle(single);

        return generateJWEToken(jacksonUtils.readTree(sessionInfo).get().toString()).get();
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<T> getSessionValue(String name) {
        return (Optional<T>) Optional.ofNullable(session.getSessionEntity().getSessionValue().get(name));
    }

    public void setSessionValue(String key, Object value) {
        session.getSessionEntity().getSessionValue().put(key,value);
        updateSession();
    }

    public void removeSessionValue(String key) {
        session.getSessionEntity().getSessionValue().remove(key);
        updateSession();
    }

    public void removeSessionValue(String key, Object value) {
        session.getSessionEntity().getSessionValue().remove(key,value);
        updateSession();
    }

    private void updateSession() {
        sessionDao.set(RedisKey.of(session.getSessionInfo().getKey()), session.getSessionEntity(), Duration.parse(session.getSessionInfo().getTimeToLive()));
    }

    public void cleanSession() {
        sessionDao.del(RedisKey.of(session.getSessionInfo().getKey()));
    }

    public Optional<String> generateJWEToken(String body) {
        try {
            return Optional.ofNullable(jwtUtils.encryptionJWEToken(body));
        } catch (JOSEException e) {
            return Optional.empty();
        }
    }

    public Optional<String> decryptionJWEToken(String token) {
        try {
            return Optional.ofNullable(jwtUtils.decryptJWEToken(token));
        } catch (JOSEException | ParseException e) {
            return Optional.empty();
        }
    }

}
