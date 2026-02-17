package se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.RelationTypeEntity;

public interface RelationTypeEntityRepository extends
    JpaRepository<RelationTypeEntity, Long> {

  Optional<RelationTypeEntity> findByRelationType(String relationType);
}