package io.github.amings.mingle.svc.session.svc;

import io.github.amings.mingle.svc.AbstractService;
import io.github.amings.mingle.svc.SvcRequest;
import io.github.amings.mingle.svc.SvcResponse;
import io.github.amings.mingle.svc.annotation.Svc;
import io.github.amings.mingle.svc.filter.SvcInfo;
import io.github.amings.mingle.svc.session.annotation.SvcSession;

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
