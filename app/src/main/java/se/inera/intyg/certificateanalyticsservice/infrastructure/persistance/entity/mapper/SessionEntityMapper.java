package se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.mapper;

import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.SessionEntity;

public class SessionEntityMapper {

  private SessionEntityMapper() {
    throw new IllegalStateException("Utility class");
  }

  public static SessionEntity map(String sessionId) {
    return SessionEntity.builder()
        .sessionId(sessionId)
        .build();
  }
}
