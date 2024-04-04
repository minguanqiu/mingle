package io.github.amings.mingle.svc;

import io.github.amings.mingle.svc.annotation.Svc;
import io.github.amings.mingle.svc.filter.SvcInfo;
import io.github.amings.mingle.svc.request.SimpleSvcReq;
import io.github.amings.mingle.svc.response.SimpleSvcRes;

/**
 * @author Ming
 */
@Svc(bodyProcess = true)
public class SimpleSvcBodyProcess extends AbstractService<SimpleSvcReq, SimpleSvcRes> {
    public SimpleSvcBodyProcess(SvcInfo svcInfo) {
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
