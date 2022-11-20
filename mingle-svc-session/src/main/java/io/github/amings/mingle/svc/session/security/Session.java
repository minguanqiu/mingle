package io.github.amings.mingle.svc.session.security;

import io.github.amings.mingle.svc.session.dao.entity.SessionEntity;
import io.github.amings.mingle.svc.session.security.model.SessionInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

/**
 * Request session object
 *
 * @author Ming
 */

@Getter
@Setter(AccessLevel.PACKAGE)
@Component
@RequestScope
public class Session {

    private SessionEntity sessionEntity;

    private SessionInfo sessionInfo;

}
