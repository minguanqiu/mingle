package io.github.minguanqiu.mingle.svc.session;

import io.github.minguanqiu.mingle.svc.AbstractService;
import io.github.minguanqiu.mingle.svc.annotation.Svc;
import io.github.minguanqiu.mingle.svc.filter.SvcInfo;
import io.github.minguanqiu.mingle.svc.session.annotation.SvcSession;
import io.github.minguanqiu.mingle.svc.session.utils.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Qiu Guan Ming
 */
@Svc
@SvcSession(types = "Login")
public class SimpleSvcSessionFeature extends AbstractService<SimpleSvcReq, SimpleSvcRes> {

  @Autowired
  private SessionUtils sessionUtils;

  public SimpleSvcSessionFeature(SvcInfo svcInfo) {
    super(svcInfo);
  }

  @Override
  public SimpleSvcRes doService(SimpleSvcReq request) {
    String value = (String) sessionUtils.getSessionValue("Hello")
        .orElseThrow(IllegalArgumentException::new);
    if (!value.equals("World")) {
      throw new IllegalArgumentException();
    }
    sessionUtils.setSessionValue("Hello", "World1");
    sessionUtils.setSessionValue("Hello2", "World2");
    sessionUtils.updateSession();
    sessionUtils.removeSessionValue("Hello2");
    sessionUtils.updateSession();
    SimpleSvcRes simpleSvcRes = new SimpleSvcRes();
    simpleSvcRes.setText1(request.getText1());
    simpleSvcRes.setText2(request.getText2());
    return simpleSvcRes;
  }

}
