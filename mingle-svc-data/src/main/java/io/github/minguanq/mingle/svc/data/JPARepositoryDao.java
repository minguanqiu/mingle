package io.github.minguanq.mingle.svc.data;

import org.springframework.data.repository.Repository;

/**
 * Base class for all dao logic
 *
 * @author Ming
 */
public abstract class JPARepositoryDao<R extends Repository<?, ?>> {

    protected R repository;

    public JPARepositoryDao(R repository) {
        this.repository = repository;
    }

}
