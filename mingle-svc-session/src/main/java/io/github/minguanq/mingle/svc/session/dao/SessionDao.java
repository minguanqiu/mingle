package io.github.minguanq.mingle.svc.session.dao;

import io.github.minguanq.mingle.svc.redis.RedisCrudRepositoryDao;
import io.github.minguanq.mingle.svc.session.dao.entity.SessionEntity;
import io.github.minguanq.mingle.svc.session.dao.repository.SessionRepository;
import org.springframework.stereotype.Service;

/**
 * Dao for session logic
 *
 * @author Ming
 */

@Service
public class SessionDao extends RedisCrudRepositoryDao<SessionRepository, SessionEntity> {

    public SessionDao(SessionRepository repository) {
        super(repository);
    }

}
