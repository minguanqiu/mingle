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
 * Utils for session feature.
 *
 * @author Qiu Guan Ming
 */
public class SessionUtils {

  private final SvcSessionDao svcSessionDao;
  private final SessionTokenHandler sessionTokenHandler;
  private final JacksonUtils jacksonUtils;

  /**
   * Create a new SessionUtils instance.
   *
   * @param svcSessionDao       the service session DAO.
   * @param sessionTokenHandler the session token handler.
   * @param jacksonUtils        the jackson utils.
   */
  public SessionUtils(SvcSessionDao svcSessionDao, SessionTokenHandler sessionTokenHandler,
      JacksonUtils jacksonUtils) {
    this.svcSessionDao = svcSessionDao;
    this.sessionTokenHandler = sessionTokenHandler;
    this.jacksonUtils = jacksonUtils;
  }

  /**
   * Generate session token.
   *
   * @param redisKey the redis key.
   * @param session  the session entity.
   * @return return token cipher text.
   */
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

  /**
   * Get session value from session value map.
   *
   * @param name the key name.
   * @param <T>  the return type.
   * @return return the value.
   */
  @SuppressWarnings("unchecked")
  public <T> Optional<T> getSessionValue(String name) {
    return (Optional<T>) Optional.ofNullable(
        getCurrentSession().getSessionValue().get(name));
  }

  /**
   * Save session value.
   *
   * @param key   the key name.
   * @param value the value.
   */
  public void setSessionValue(String key, Object value) {
    getCurrentSession().getSessionValue().put(key, value);
  }

  /**
   * Remove session value.
   *
   * @param key the key name.
   */
  public void removeSessionValue(String key) {
    getCurrentSession().getSessionValue().remove(key);
  }

  /**
   * Update session to redis.
   */
  public void updateSession() {
    svcSessionDao.set(getCurrentSession());
  }

  /**
   * Remove session.
   */
  public void cleanSession() {
    svcSessionDao.delete(getCurrentSession().getRedisKey());
  }

  /**
   * Get current session from {@link SecurityContextHolder}.
   *
   * @return return current session entity.
   */
  public SvcSessionEntity getCurrentSession() {
    return (SvcSessionEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }

}
