package se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.mapper;

import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.UserEntity;

public class UserEntityMapper {

  private UserEntityMapper() {
    throw new IllegalStateException("Utility class");
  }
  
  public static UserEntity map(String userId) {
    return UserEntity.builder()
        .userId(userId)
        .build();
  }
}
