package se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.EventTypeEntity;

@Repository
public interface EventTypeEntityRepository extends CrudRepository<EventTypeEntity, Long> {

  Optional<EventTypeEntity> findByEventType(String eventType);
}

