package io.github.minguanqiu.mingle.svc.redis;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Qiu Guan Ming
 */
@SpringBootTest
@ComponentScan
public class RedisTests {

  @Autowired
  private SimpleRepositoryDao simpleRepositoryDao;
  @Autowired
  private RedisKeyTemplate<SimpleEntity> redisKeyTemplate;
  @Autowired
  private RedisKeyTemplateDaoImpl redisKeyTemplateDao;

  @Test
  void testRepository() {
    RedisKey redisKey = RedisKey.builder().addParam(UUID.randomUUID().toString()).build();
    SimpleEntity simpleEntity = new SimpleEntity(redisKey, 123);
    simpleEntity.setText1("H");
    simpleRepositoryDao.save(simpleEntity);
    Optional<SimpleEntity> simple2Entity1 = simpleRepositoryDao.findById(redisKey);
    assertThat(simple2Entity1.isPresent()).isTrue();
    assertThat(simple2Entity1.get().getText1()).isEqualTo("H");
  }

  @Test
  void testTemplate() {
    RedisKey redisKey = RedisKey.builder().addParam(UUID.randomUUID().toString()).build();
    SimpleEntity simpleEntity = new SimpleEntity(redisKey, 600);
    simpleEntity.setText1("Hello");
    redisKeyTemplate.opsForValue().set(redisKey, simpleEntity, Duration.ofMinutes(1));
    SimpleEntity entity = redisKeyTemplate.opsForValue().get(redisKey);
    assertThat(entity).isNotNull();
    assertThat(entity.getText1()).isEqualTo(simpleEntity.getText1());
    assertThat(entity.getRedisKey().toString()).isEqualTo(redisKey.toString());
    assertThat(entity.getTimeToLive()).isEqualTo(simpleEntity.getTimeToLive());
  }

  @Test
  void testTemplateDao() {
    RedisKey redisKey = RedisKey.builder().addParam(UUID.randomUUID().toString()).build();
    SimpleEntity simpleEntity = new SimpleEntity(redisKey, 600);
    simpleEntity.setText1("Hello");
    redisKeyTemplateDao.set(simpleEntity);
    SimpleEntity entity = redisKeyTemplateDao.get(redisKey);
    assertThat(entity).isNotNull();
    assertThat(entity.getText1()).isEqualTo(simpleEntity.getText1());
    assertThat(entity.getRedisKey().toString()).isEqualTo(redisKey.toString());
    assertThat(entity.getTimeToLive()).isEqualTo(simpleEntity.getTimeToLive());
  }

}
