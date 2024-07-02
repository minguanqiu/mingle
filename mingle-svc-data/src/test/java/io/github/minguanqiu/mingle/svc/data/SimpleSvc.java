package io.github.minguanqiu.mingle.svc.data;

import io.github.minguanqiu.mingle.svc.AbstractService;
import io.github.minguanqiu.mingle.svc.annotation.Svc;
import io.github.minguanqiu.mingle.svc.data.dao.TestDao;
import io.github.minguanqiu.mingle.svc.data.dao.entity.TestEntity;
import io.github.minguanqiu.mingle.svc.filter.SvcInfo;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Qiu Guan Ming
 */
@Svc
public class SimpleSvc extends AbstractService<SimpleSvcReq, SimpleSvcRes> {

  @Autowired
  TestDao testDao;

  public SimpleSvc(SvcInfo svcInfo) {
    super(svcInfo);
  }

  @Override
  public SimpleSvcRes doService(SimpleSvcReq request) {
    TestEntity testEntity = new TestEntity();
    testEntity.setSerial("1");
    testEntity.setText1("Hello");
    testEntity.setText2("World");
    testDao.save(testEntity);
    Optional<TestEntity> id = testDao.findById("1");
    TestEntity testEntity1 = id.get();
    testDao.delete(testEntity1);
    return new SimpleSvcRes();
  }


}
