package io.github.minguanqiu.mingle.svc.data;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * A {@link CrudRepository} impl provide crud base method
 *
 * @author Ming
 */
public class JPACrudRepositoryDao<R extends CrudRepository<T, ID>, T, ID> extends JPARepositoryDao<R> {

    public JPACrudRepositoryDao(R repository) {
        super(repository);
    }

    public <S extends T> S save(S entity) {
        return repository.save(entity);
    }

    public <S extends T> Iterable<S> saveAll(Iterable<S> entities) {
        return repository.saveAll(entities);
    }

    public Optional<T> findById(ID id) {
        return repository.findById(id);
    }

    public boolean existsById(ID id) {
        return repository.existsById(id);
    }

    public Iterable<T> findAll() {
        return repository.findAll();
    }

    public Iterable<T> findAllById(Iterable<ID> ids) {
        return repository.findAllById(ids);
    }

    public long count() {
        return repository.count();
    }

    public void deleteById(ID id) {
        repository.deleteById(id);
    }

    public void delete(T entity) {
        repository.delete(entity);
    }

    public void deleteAllById(Iterable<? extends ID> ids) {
        repository.deleteAllById(ids);
    }

    public void deleteAll(Iterable<? extends T> entities) {
        repository.deleteAll(entities);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

}
