package io.github.minguanq.mingle.svc.session;

import io.github.minguanq.mingle.svc.session.dao.entity.SessionEntity;

/**
 * @author Ming
 */
public record SessionInfo(SessionHeader sessionHeader, SessionEntity session) {


}
