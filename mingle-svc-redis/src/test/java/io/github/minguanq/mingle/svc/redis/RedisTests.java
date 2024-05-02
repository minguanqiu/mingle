package io.github.minguanq.mingle.svc.redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Ming
 */
@SpringBootTest
@ComponentScan
@EnableRedisRepositories
public class RedisTests {

    @Autowired
    private SimpleRepositoryDao simpleRepositoryDao;

    @Test
    void testRedisRepository() {
        RedisKey redisKey = RedisKey.builder().addParam("simple2").addParam("123").build();
        SimpleEntity simpleEntity = new SimpleEntity(redisKey, 123);
        simpleEntity.setText1("H");
        simpleRepositoryDao.save(simpleEntity);
        Optional<SimpleEntity> simple2Entity1 = simpleRepositoryDao.findById(redisKey);
        assertThat(simple2Entity1.isPresent()).isTrue();
        assertThat(simple2Entity1.get().getText1()).isEqualTo("H");
    }
}
