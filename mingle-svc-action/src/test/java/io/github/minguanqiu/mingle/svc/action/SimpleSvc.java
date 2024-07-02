package io.github.minguanqiu.mingle.svc.action;

import io.github.minguanqiu.mingle.svc.AbstractService;
import io.github.minguanqiu.mingle.svc.action.enums.AutoBreak;
import io.github.minguanqiu.mingle.svc.annotation.Svc;
import io.github.minguanqiu.mingle.svc.filter.SvcInfo;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Qiu Guan Ming
 */
@Svc
public class SimpleSvc extends AbstractService<SimpleSvcReq, SimpleSvcRes> {

  @Autowired
  SimpleAction simpleAction;

  public SimpleSvc(SvcInfo svcInfo) {
    super(svcInfo);
  }

  @Override
  public SimpleSvcRes doService(SimpleSvcReq request) {
    SimpleSvcRes simpleSvcRes = new SimpleSvcRes();
    ActionResponse<SimpleActionRes> simpleActionResActionResponse = simpleAction.doAction(
        simpleActionReq(request));
    simpleSvcRes.setText1(request.getText1());
    simpleSvcRes.setText2(request.getText2());
    return simpleSvcRes;
  }

  private SimpleActionReq simpleActionReq(SimpleSvcReq request) {
    SimpleActionReq simpleActionReq = new SimpleActionReq();
    if(request.getAction().equals("true")) {
      simpleActionReq.setAutoBreak(AutoBreak.TRUE);
    }
    simpleActionReq.setText1(request.getText1());
    simpleActionReq.setText2(request.getText2());
    return simpleActionReq;
  }

}
