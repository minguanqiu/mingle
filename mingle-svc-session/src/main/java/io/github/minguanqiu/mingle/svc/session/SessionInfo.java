package io.github.minguanqiu.mingle.svc.session;

import io.github.minguanqiu.mingle.svc.session.dao.entity.SessionEntity;

/**
 * @author Ming
 */
public record SessionInfo(SessionHeader sessionHeader, SessionEntity session) {


}
