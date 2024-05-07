package io.github.minguanqiu.mingle.svc.data.dao;

import io.github.minguanqiu.mingle.svc.data.JPACrudRepositoryDao;
import io.github.minguanqiu.mingle.svc.data.dao.entity.TestEntity;
import io.github.minguanqiu.mingle.svc.data.dao.repository.TestRepository;
import org.springframework.stereotype.Service;

/**
 * @author Ming
 */
@Service
public class TestDao extends JPACrudRepositoryDao<TestRepository, TestEntity, String> {

    public TestDao(TestRepository repository) {
        super(repository);
    }

}
