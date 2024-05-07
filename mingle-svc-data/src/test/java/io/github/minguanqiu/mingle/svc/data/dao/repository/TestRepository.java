package io.github.minguanqiu.mingle.svc.data.dao.repository;

import io.github.minguanqiu.mingle.svc.data.dao.entity.TestEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Ming
 */
public interface TestRepository extends CrudRepository<TestEntity, String> {
}
