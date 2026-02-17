package se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.UnitEntity;

@Repository
public interface UnitEntityRepository extends CrudRepository<UnitEntity, Long> {

  Optional<UnitEntity> findByHsaId(String hsaId);
}

