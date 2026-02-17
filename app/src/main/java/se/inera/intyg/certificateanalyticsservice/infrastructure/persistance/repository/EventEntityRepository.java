package se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.EventEntity;

@Repository
public interface EventEntityRepository extends CrudRepository<EventEntity, Integer> {

  Optional<EventEntity> findByMessageId(String messageId);
}
