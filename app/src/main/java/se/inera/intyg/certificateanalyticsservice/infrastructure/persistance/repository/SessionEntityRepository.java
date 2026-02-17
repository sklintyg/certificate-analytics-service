package se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.SessionEntity;

@Repository
public interface SessionEntityRepository extends CrudRepository<SessionEntity, Long> {

  Optional<SessionEntity> findBySessionId(String sessionId);
}

