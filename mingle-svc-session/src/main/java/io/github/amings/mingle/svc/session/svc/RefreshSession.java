package io.github.amings.mingle.svc.session.svc;

import io.github.amings.mingle.svc.AbstractSvcLogic;
import io.github.amings.mingle.svc.SvcReqModel;
import io.github.amings.mingle.svc.SvcResModel;
import io.github.amings.mingle.svc.annotation.Svc;
import io.github.amings.mingle.svc.session.annotation.Session;

/**
 * Svc for refresh session
 *
 * @author Ming
 */

@Session("refresh")
@Svc(desc = "RefreshSession")
public class RefreshSession extends AbstractSvcLogic<SvcReqModel, SvcResModel> {

    @Override
    public SvcResModel doService(SvcReqModel reqModel, SvcResModel resModel) {
        return resModel;
    }
}
