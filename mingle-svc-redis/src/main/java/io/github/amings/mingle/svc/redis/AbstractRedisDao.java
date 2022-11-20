package io.github.amings.mingle.svc.redis;

import io.github.amings.mingle.utils.ReflectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Base class for all redis dao class
 *
 * @author Ming
 */

@Service
public abstract class AbstractRedisDao<R extends Redis> {

    @Autowired
    ApplicationContext applicationContext;

    protected R redis;

    @PostConstruct
    @SuppressWarnings("unchecked")
    private void init() {
        redis = (R) applicationContext.getBean(ReflectionUtils.getGenericClassWithGeneric(this.getClass(), 0));
    }

}
