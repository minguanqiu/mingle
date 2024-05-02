package io.github.minguanq.mingle.svc.data;

import io.github.minguanq.mingle.svc.data.dao.TestDao;
import io.github.minguanq.mingle.svc.data.dao.entity.TestEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Ming
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
