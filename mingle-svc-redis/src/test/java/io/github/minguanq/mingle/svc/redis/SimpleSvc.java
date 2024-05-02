package io.github.minguanq.mingle.svc.redis;

import io.github.minguanq.mingle.svc.AbstractService;
import io.github.minguanq.mingle.svc.annotation.Svc;
import io.github.minguanq.mingle.svc.filter.SvcInfo;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Ming
 */
@Svc
public class SimpleSvc extends AbstractService<SimpleSvcReq, SimpleSvcRes> {

    @Autowired
    SimpleRepositoryDao simpleRepositoryDao;

    public SimpleSvc(SvcInfo svcInfo) {
        super(svcInfo);
    }

    @Override
    public SimpleSvcRes doService(SimpleSvcReq request) {
        RedisKey build = RedisKey.builder().addParam("test").build();
        SimpleEntity simpleEntity = new SimpleEntity(build, 1000);
        simpleRepositoryDao.save(simpleEntity);
        return new SimpleSvcRes();
    }


}
