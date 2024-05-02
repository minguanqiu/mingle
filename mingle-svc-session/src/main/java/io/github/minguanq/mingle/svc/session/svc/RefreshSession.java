package io.github.minguanq.mingle.svc.session.svc;

import io.github.minguanq.mingle.svc.AbstractService;
import io.github.minguanq.mingle.svc.SvcRequest;
import io.github.minguanq.mingle.svc.SvcResponseBody;
import io.github.minguanq.mingle.svc.annotation.Svc;
import io.github.minguanq.mingle.svc.filter.SvcInfo;
import io.github.minguanq.mingle.svc.session.annotation.SvcSession;

/**
 * Svc for refresh session
 *
 * @author Ming
 */

@SvcSession(types = "refresh")
@Svc(description = "RefreshSession")
public class RefreshSession extends AbstractService<SvcRequest, SvcResponseBody> {

    public RefreshSession(SvcInfo svcInfo) {
        super(svcInfo);
    }

    @Override
    public SvcResponseBody doService(SvcRequest reqModel) {
        return new SvcResponseBody();
    }
}
