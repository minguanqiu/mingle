package io.github.amings.mingle.svc.data.dao;

import io.github.amings.mingle.svc.data.annotation.DaoService;
import io.github.amings.mingle.utils.ReflectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.Repository;

import javax.annotation.PostConstruct;

/**
 * Base class for all database class
 *
 * @author Ming
 */

@DaoService
public abstract class AbstractDao<T extends Repository> {
    @Autowired
    ApplicationContext context;

    protected T repository;

    @SuppressWarnings("unchecked")
    @PostConstruct
    private void init() {
        repository = (T) context.getBean(ReflectionUtils.getGenericClass(this.getClass(), 0));
    }

}
