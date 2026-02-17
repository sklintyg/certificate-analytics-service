package se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.CareProviderEntity;

@Repository
public interface CareProviderEntityRepository extends CrudRepository<CareProviderEntity, Long> {

  Optional<CareProviderEntity> findByHsaId(String hsaId);
}

