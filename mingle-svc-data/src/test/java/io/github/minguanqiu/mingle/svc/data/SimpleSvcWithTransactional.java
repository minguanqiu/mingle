package io.github.minguanqiu.mingle.svc.data;

import io.github.minguanqiu.mingle.svc.AbstractService;
import io.github.minguanqiu.mingle.svc.annotation.Svc;
import io.github.minguanqiu.mingle.svc.filter.SvcInfo;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Qiu Guan Ming
 */
@Svc
public class SimpleSvcWithTransactional extends AbstractService<SimpleSvcReq, SimpleSvcRes> {

  public SimpleSvcWithTransactional(SvcInfo svcInfo) {
    super(svcInfo);
  }

  @Transactional
  @Override
  public SimpleSvcRes doService(SimpleSvcReq request) {
    return new SimpleSvcRes();
  }


}
