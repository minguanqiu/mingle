package io.github.amings.mingle.svc.session.svc;

import io.github.amings.mingle.svc.AbstractSvcLogic;
import io.github.amings.mingle.svc.SvcReqModel;
import io.github.amings.mingle.svc.SvcResModel;
import io.github.amings.mingle.svc.annotation.Svc;
import io.github.amings.mingle.svc.configuration.properties.SvcProperties;
import io.github.amings.mingle.svc.filter.SvcInfo;
import io.github.amings.mingle.svc.handler.SvcMsgHandler;
import io.github.amings.mingle.svc.session.annotation.Session;

/**
 * Svc for refresh session
 *
 * @author Ming
 */

@Session("refresh")
@Svc(desc = "RefreshSession", log = false)
public class RefreshSession extends AbstractSvcLogic<SvcReqModel, SvcResModel> {

    public RefreshSession(SvcInfo svcInfo, SvcMsgHandler svcMsgHandler, SvcProperties svcProperties) {
        super(svcInfo, svcMsgHandler, svcProperties);
    }

    @Override
    public SvcResModel doService(SvcReqModel reqModel, SvcResModel resModel) {
        return resModel;
    }
}
