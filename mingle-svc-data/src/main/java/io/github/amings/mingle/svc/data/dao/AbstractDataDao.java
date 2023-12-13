package io.github.amings.mingle.svc.data.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.repository.Repository;

/**
 * Base class for all dao logic class
 *
 * @author Ming
 */

public abstract class AbstractDataDao<T extends Repository> extends AbstractDao<T> {

    @PersistenceContext
    protected EntityManager entityManager;

}
