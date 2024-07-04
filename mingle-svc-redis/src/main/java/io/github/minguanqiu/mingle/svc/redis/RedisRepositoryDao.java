package io.github.minguanqiu.mingle.svc.redis;

import org.springframework.data.repository.Repository;

/**
 * Base class for all redis repository DAO.
 *
 * @param <R> the spring repository.
 * @author Qiu Guan Ming
 */
public class RedisRepositoryDao<R extends Repository<? extends RedisEntity, RedisKey>> extends
    RedisDao {

  /**
   * Spring repository.
   */
  protected final R repository;

  /**
   * Create a new RedisRepositoryDao instance.
   *
   * @param repository the spring repository.
   */
  public RedisRepositoryDao(R repository) {
    this.repository = repository;
  }

}
