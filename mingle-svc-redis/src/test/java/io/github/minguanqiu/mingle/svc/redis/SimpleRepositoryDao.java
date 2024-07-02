package io.github.minguanqiu.mingle.svc.redis;

import org.springframework.stereotype.Service;

/**
 * @author Qiu Guan Ming
 */
@Service
public class SimpleRepositoryDao extends RedisCrudRepositoryDao<SimpleRepository, SimpleEntity> {

  public SimpleRepositoryDao(SimpleRepository repository) {
    super(repository);
  }

}
