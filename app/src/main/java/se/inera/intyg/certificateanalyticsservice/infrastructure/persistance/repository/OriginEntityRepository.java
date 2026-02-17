package se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.OriginEntity;

@Repository
public interface OriginEntityRepository extends CrudRepository<OriginEntity, Long> {

  Optional<OriginEntity> findByOrigin(String origin);
}

