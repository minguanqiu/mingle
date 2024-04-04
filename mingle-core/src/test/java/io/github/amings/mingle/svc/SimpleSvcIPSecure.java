package io.github.amings.mingle.svc;

import io.github.amings.mingle.svc.annotation.Svc;
import io.github.amings.mingle.svc.filter.SvcInfo;
import io.github.amings.mingle.svc.request.SimpleSvcReq;
import io.github.amings.mingle.svc.response.SimpleSvcRes;

/**
 * @author Ming
 */
@Svc(ipSecure = true)
public class SimpleSvcIPSecure extends AbstractService<SimpleSvcReq, SimpleSvcRes> {
    public SimpleSvcIPSecure(SvcInfo svcInfo) {
        super(svcInfo);
    }

    @Override
    public SimpleSvcRes doService(SimpleSvcReq request) {
        SimpleSvcRes simpleSvcRes = new SimpleSvcRes();
        simpleSvcRes.setText1(request.getText1());
        simpleSvcRes.setText2(request.getText2());
        return simpleSvcRes;
    }

}
