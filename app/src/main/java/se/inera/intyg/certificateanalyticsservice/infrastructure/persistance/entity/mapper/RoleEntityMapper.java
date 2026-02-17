package se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.mapper;

import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.RoleEntity;

public class RoleEntityMapper {

  private RoleEntityMapper() {
    throw new IllegalStateException("Utility class");
  }

  public static RoleEntity map(String role) {
    return RoleEntity.builder()
        .role(role)
        .build();
  }
}
