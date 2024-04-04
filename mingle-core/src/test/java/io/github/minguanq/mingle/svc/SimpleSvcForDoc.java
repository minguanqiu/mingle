package io.github.minguanq.mingle.svc;

import io.github.minguanq.mingle.svc.annotation.Svc;
import io.github.minguanq.mingle.svc.filter.SvcInfo;
import io.github.minguanq.mingle.svc.request.SimpleSvcReq;
import io.github.minguanq.mingle.svc.response.SimpleSvcRes;

/**
 * @author Ming
 */
@Svc(tags = "tests",summary = "test service for summary",description = "test service for description")
public class SimpleSvcForDoc extends AbstractService<SimpleSvcReq, SimpleSvcRes> {
    public SimpleSvcForDoc(SvcInfo svcInfo) {
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
