package io.github.minguanqiu.mingle.svc;

import io.github.minguanqiu.mingle.svc.annotation.Svc;
import io.github.minguanqiu.mingle.svc.annotation.SvcFeature;
import io.github.minguanqiu.mingle.svc.filter.SvcInfo;
import io.github.minguanqiu.mingle.svc.request.SimpleSvcReq;
import io.github.minguanqiu.mingle.svc.response.SimpleSvcRes;

/**
 * @author Ming
 */
@SvcFeature
@Svc
public class SimpleSvcWithFeature1 extends AbstractService<SimpleSvcReq, SimpleSvcRes> {
    public SimpleSvcWithFeature1(SvcInfo svcInfo) {
        super(svcInfo);
    }

    @Override
    public SimpleSvcRes doService(SimpleSvcReq request) {
        SimpleSvcRes simpleSvcRes = new SimpleSvcRes();
        simpleSvcRes.setText2(request.getText2());
        return simpleSvcRes;
    }

}


