package io.github.minguanq.mingle.svc.redis;

import org.springframework.stereotype.Service;

/**
 * @author Ming
 */
@Service
public class SimpleRepositoryDao extends RedisCrudRepositoryDao<SimpleRepository,SimpleEntity> {

    public SimpleRepositoryDao(SimpleRepository repository) {
        super(repository);
    }

}
