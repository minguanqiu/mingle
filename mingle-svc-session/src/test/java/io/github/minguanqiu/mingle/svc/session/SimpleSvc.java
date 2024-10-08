package io.github.minguanqiu.mingle.svc.session;

import io.github.minguanqiu.mingle.svc.AbstractService;
import io.github.minguanqiu.mingle.svc.annotation.Svc;
import io.github.minguanqiu.mingle.svc.filter.SvcInfo;
import io.github.minguanqiu.mingle.svc.session.annotation.SvcSession;

/**
 * @author Qiu Guan Ming
 */
@Svc
@SvcSession(types = "Login")
public class SimpleSvc extends AbstractService<SimpleSvcReq, SimpleSvcRes> {

  public SimpleSvc(SvcInfo svcInfo) {
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
