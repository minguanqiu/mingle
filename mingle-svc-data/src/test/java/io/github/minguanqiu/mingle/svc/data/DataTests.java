package io.github.minguanqiu.mingle.svc.data;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.minguanqiu.mingle.svc.data.dao.TestDao;
import io.github.minguanqiu.mingle.svc.data.dao.entity.TestEntity;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Qiu Guan Ming
 */
@SpringBootTest
public class DataTests {

  @Autowired
  TestDao testDao;

  @Test
  void testJPADao() {
    TestEntity testEntity = new TestEntity();
    testEntity.setSerial("1");
    testEntity.setText1("Hello");
    testEntity.setText2("World");
    testDao.save(testEntity);
    Optional<TestEntity> id = testDao.findById("1");
    TestEntity testEntity1 = id.get();
    assertThat(testEntity.getText1()).isEqualTo("Hello");
    assertThat(testEntity.getText2()).isEqualTo("World");
    testDao.delete(testEntity1);
    assertThat(testDao.findById("1")).isNotPresent();
  }

}
