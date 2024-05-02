package io.github.minguanq.mingle.svc;

import io.github.minguanq.mingle.svc.annotation.Svc;
import io.github.minguanq.mingle.svc.annotation.SvcFeature;
import io.github.minguanq.mingle.svc.filter.SvcInfo;
import io.github.minguanq.mingle.svc.request.SimpleSvcReq;
import io.github.minguanq.mingle.svc.response.SimpleSvcRes;

/**
 * @author Ming
 */
@SvcFeature(logging = true, body_process = true, ip_secure = "127.0.0.1")
@Svc
public class SimpleSvcWithFeature extends AbstractService<SimpleSvcReq, SimpleSvcRes> {
    public SimpleSvcWithFeature(SvcInfo svcInfo) {
        super(svcInfo);
    }

    @Override
    public SimpleSvcRes doService(SimpleSvcReq request) {
        SimpleSvcRes simpleSvcRes = new SimpleSvcRes();
        simpleSvcRes.setText2(request.getText2());
        return simpleSvcRes;
    }

}


