package io.github.minguanqiu.mingle.svc.session.handler;

import io.github.minguanqiu.mingle.svc.session.dao.entity.SvcSessionEntity;

/**
 * @author Qiu Guan Ming
 */
public interface SessionHandler {

  SvcSessionEntity getSession(String token);

  void afterAuthentication(SvcSessionEntity svcSession);

}
