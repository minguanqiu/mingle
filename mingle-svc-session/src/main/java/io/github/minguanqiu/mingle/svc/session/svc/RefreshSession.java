package io.github.minguanqiu.mingle.svc.session.svc;

import io.github.minguanqiu.mingle.svc.AbstractService;
import io.github.minguanqiu.mingle.svc.SvcRequest;
import io.github.minguanqiu.mingle.svc.SvcResponseBody;
import io.github.minguanqiu.mingle.svc.annotation.Svc;
import io.github.minguanqiu.mingle.svc.filter.SvcInfo;
import io.github.minguanqiu.mingle.svc.session.annotation.SvcSession;

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
