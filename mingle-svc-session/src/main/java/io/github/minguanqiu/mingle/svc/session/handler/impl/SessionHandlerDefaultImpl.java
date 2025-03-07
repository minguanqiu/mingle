package io.github.minguanqiu.mingle.svc.session.handler.impl;

import io.github.minguanqiu.mingle.svc.session.SessionHeader;
import io.github.minguanqiu.mingle.svc.session.dao.SvcSessionDao;
import io.github.minguanqiu.mingle.svc.session.dao.entity.SvcSessionEntity;
import io.github.minguanqiu.mingle.svc.session.exception.SessionHeaderDeserializeErrorException;
import io.github.minguanqiu.mingle.svc.session.handler.SessionHandler;
import io.github.minguanqiu.mingle.svc.utils.JacksonUtils;

/**
 * @author Qiu Guan Ming
 */
public class SessionHandlerDefaultImpl implements SessionHandler {

  private SvcSessionDao svcSessionDao;
  private final JacksonUtils jacksonUtils;

  public SessionHandlerDefaultImpl(SvcSessionDao svcSessionDao, JacksonUtils jacksonUtils) {
    this.svcSessionDao = svcSessionDao;
    this.jacksonUtils = jacksonUtils;
  }

  @Override
  public SvcSessionEntity getSession(String token) {
    SessionHeader sessionHeader = jacksonUtils.readValue(token, SessionHeader.class)
        .orElseThrow(SessionHeaderDeserializeErrorException::new);
    return svcSessionDao.get(sessionHeader.redisKey());
  }

  @Override
  public void afterAuthentication(SvcSessionEntity svcSessionEntity) {
    svcSessionDao.set(svcSessionEntity); // refresh session with origin live time
  }
}
