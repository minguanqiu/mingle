package io.github.minguanqiu.mingle.svc.redis;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

/**
 * A {@link CrudRepository} implementations provide CRUD base method.
 *
 * @param <R> the spring repository.
 * @param <T> the redis entity.
 * @author Qiu Guan Ming
 */
public class RedisCrudRepositoryDao<R extends CrudRepository<T, RedisKey>, T extends RedisEntity> extends
    RedisRepositoryDao<R> {

  /**
   * Create a new RedisCrudRepositoryDao instance.
   *
   * @param repository the spring repository.
   */
  public RedisCrudRepositoryDao(R repository) {
    super(repository);
  }

  public <S extends T> S save(S entity) {
    return repository.save(entity);
  }

  public <S extends T> Iterable<S> saveAll(Iterable<S> entities) {
    return repository.saveAll(entities);
  }

  public Optional<T> findById(RedisKey id) {
    return repository.findById(id);
  }

  public boolean existsById(RedisKey id) {
    return repository.existsById(id);
  }

  public Iterable<T> findAll() {
    return repository.findAll();
  }

  public Iterable<T> findAllById(Iterable<RedisKey> ids) {
    return repository.findAllById(ids);
  }

  public long count() {
    return repository.count();
  }

  public void deleteById(RedisKey id) {
    repository.deleteById(id);
  }

  public void delete(T entity) {
    repository.delete(entity);
  }

  public void deleteAllById(Iterable<? extends RedisKey> ids) {
    repository.deleteAllById(ids);
  }

  public void deleteAll(Iterable<? extends T> entities) {
    repository.deleteAll(entities);
  }

  public void deleteAll() {
    repository.deleteAll();
  }

}
