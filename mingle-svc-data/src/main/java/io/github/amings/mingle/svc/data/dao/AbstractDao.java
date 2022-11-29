package io.github.amings.mingle.svc.data.dao;

import com.google.common.reflect.TypeToken;
import io.github.amings.mingle.svc.data.annotation.DaoService;
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
public abstract class AbstractDao<R extends Repository> {
    @Autowired
    ApplicationContext context;
    protected R repository;
    private final Class<R> repositoryClass;

    @SuppressWarnings("unchecked")
    public AbstractDao() {
        TypeToken<R> repositoryTypeToken = new TypeToken<R>((getClass())) {
        };
        repositoryClass = (Class<R>) repositoryTypeToken.getRawType();
    }

    @PostConstruct
    private void init() {
        repository = context.getBean(repositoryClass);
    }

}
