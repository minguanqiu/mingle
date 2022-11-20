package io.github.amings.mingle.svc.data.dao;

import org.springframework.data.repository.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Base class for all dao logic class
 *
 * @author Ming
 */

public abstract class AbstractDataDao<T extends Repository> extends AbstractDao<T> {

    @PersistenceContext
    protected EntityManager entityManager;

}
