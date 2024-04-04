package io.github.minguanq.mingle.svc.session.svc;

import io.github.minguanq.mingle.svc.AbstractService;
import io.github.minguanq.mingle.svc.SvcRequest;
import io.github.minguanq.mingle.svc.SvcResponse;
import io.github.minguanq.mingle.svc.annotation.Svc;
import io.github.minguanq.mingle.svc.filter.SvcInfo;
import io.github.minguanq.mingle.svc.session.annotation.SvcSession;

/**
 * Svc for refresh session
 *
 * @author Ming
 */

@SvcSession(type = "refresh")
@Svc(description = "RefreshSession", logging = false)
public class RefreshSession extends AbstractService<SvcRequest, SvcResponse> {

    public RefreshSession(SvcInfo svcInfo) {
        super(svcInfo);
    }

    @Override
    public SvcResponse doService(SvcRequest reqModel) {
        return new SvcResponse();
    }
}
