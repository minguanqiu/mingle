package io.github.minguanqiu.mingle.svc.session.handler.impl;

import io.github.minguanqiu.mingle.svc.session.dao.entity.SvcSessionEntity;
import io.github.minguanqiu.mingle.svc.session.handler.SessionHandler;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * @author Qiu Guan Ming
 */

@Profile("test-session-handler")
@Component
public class SessionHandlerImpl implements SessionHandler {

  @Override
  public SvcSessionEntity getSession(String token) {
    return null;
  }

  @Override
  public void afterAuthentication(SvcSessionEntity svcSession) {

  }
}
