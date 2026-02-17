package se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.mapper;

import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.RelationTypeEntity;

public class RelationTypeEntityMapper {

  private RelationTypeEntityMapper() {
    throw new IllegalStateException("Utility class");
  }

  public static RelationTypeEntity map(String relationType) {
    return RelationTypeEntity.builder()
        .relationType(relationType)
        .build();
  }
}
