package io.github.minguanqiu.mingle.svc.data;

import org.springframework.data.repository.Repository;

/**
 * Base class for all dao logic
 *
 * @author Qiu Guan Ming
 */
public abstract class JPARepositoryDao<R extends Repository<?, ?>> {

  protected R repository;

  public JPARepositoryDao(R repository) {
    this.repository = repository;
  }

}
