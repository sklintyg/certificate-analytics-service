package se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.RelationTypeEntity;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.mapper.RelationTypeEntityMapper;

@Repository
@RequiredArgsConstructor
public class RelationTypeRepository {

  private final RelationTypeEntityRepository relationTypeEntityRepository;

  public RelationTypeEntity findOrCreate(String relationType) {
    if (relationType == null || relationType.isBlank()) {
      return null;
    }

    return relationTypeEntityRepository.findByRelationType(relationType)
        .orElseGet(
            () -> relationTypeEntityRepository.save(RelationTypeEntityMapper.map(relationType))
        );
  }
}
