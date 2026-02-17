package se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.PartyEntity;

@Repository
public interface PartyEntityRepository extends CrudRepository<PartyEntity, Long> {

  Optional<PartyEntity> findByParty(String party);
}

