package io.github.minguanqiu.mingle.svc.session.utils;

import io.github.minguanqiu.mingle.svc.redis.RedisKey;
import io.github.minguanqiu.mingle.svc.session.SessionHeader;
import io.github.minguanqiu.mingle.svc.session.dao.SvcSessionDao;
import io.github.minguanqiu.mingle.svc.session.dao.entity.SvcSessionEntity;
import io.github.minguanqiu.mingle.svc.session.exception.SessionTokenEncryptionErrorException;
import io.github.minguanqiu.mingle.svc.session.handler.SessionTokenHandler;
import io.github.minguanqiu.mingle.svc.utils.JacksonUtils;
import java.util.Optional;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Utils for session feature
 *
 * @author Qiu Guan Ming
 */
public class SessionUtils {

  private final SvcSessionDao svcSessionDao;
  private final SessionTokenHandler sessionTokenHandler;
  private final JacksonUtils jacksonUtils;

  public SessionUtils(SvcSessionDao svcSessionDao, SessionTokenHandler sessionTokenHandler,
      JacksonUtils jacksonUtils) {
    this.svcSessionDao = svcSessionDao;
    this.sessionTokenHandler = sessionTokenHandler;
    this.jacksonUtils = jacksonUtils;
  }

  public String createSessionToken(RedisKey redisKey, SvcSessionEntity session) {
    String encryption;
    try {
      SessionHeader sessionHeader = new SessionHeader(redisKey,
          String.valueOf(session.getTimeToLive()));
      encryption = sessionTokenHandler.encryption(
          jacksonUtils.readTree(sessionHeader).get().toString());
    } catch (Exception e) {
      throw new SessionTokenEncryptionErrorException(e);
    }
    svcSessionDao.set(session);
    return encryption;
  }


  @SuppressWarnings("unchecked")
  public <T> Optional<T> getSessionValue(String name) {
    return (Optional<T>) Optional.ofNullable(
        getCurrentSession().getSessionValue().get(name));
  }

  public void setSessionValue(String key, Object value) {
    getCurrentSession().getSessionValue().put(key, value);
  }

  public void removeSessionValue(String key) {
    getCurrentSession().getSessionValue().remove(key);
  }

  public void updateSession() {
    svcSessionDao.set(getCurrentSession());
  }

  public void cleanSession() {
    svcSessionDao.delete(getCurrentSession().getRedisKey());
  }

  public SvcSessionEntity getCurrentSession() {
    return (SvcSessionEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }

}
