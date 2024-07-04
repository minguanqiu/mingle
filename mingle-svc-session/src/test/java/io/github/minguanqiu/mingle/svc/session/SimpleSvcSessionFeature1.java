package io.github.minguanqiu.mingle.svc.session;

import io.github.minguanqiu.mingle.svc.AbstractService;
import io.github.minguanqiu.mingle.svc.annotation.Svc;
import io.github.minguanqiu.mingle.svc.filter.SvcInfo;
import io.github.minguanqiu.mingle.svc.session.annotation.SvcSession;
import io.github.minguanqiu.mingle.svc.session.utils.SessionUtils;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Qiu Guan Ming
 */
@Svc
@SvcSession(types = "Login")
public class SimpleSvcSessionFeature1 extends AbstractService<SimpleSvcReq, SimpleSvcRes> {

  @Autowired
  private SessionUtils sessionUtils;

  public SimpleSvcSessionFeature1(SvcInfo svcInfo) {
    super(svcInfo);
  }

  @Override
  public SimpleSvcRes doService(SimpleSvcReq request) {
    String value = (String) sessionUtils.getSessionValue("Hello")
        .orElseThrow(IllegalArgumentException::new);
    if (!value.equals("World1")) {
      throw new IllegalArgumentException();
    }
    Optional<String> value2 = sessionUtils.getSessionValue("Hello2");
    if (value2.isPresent()) {
      throw new IllegalArgumentException();
    }
    sessionUtils.cleanSession();
    SimpleSvcRes simpleSvcRes = new SimpleSvcRes();
    simpleSvcRes.setText1(request.getText1());
    simpleSvcRes.setText2(request.getText2());
    return simpleSvcRes;
  }

}
