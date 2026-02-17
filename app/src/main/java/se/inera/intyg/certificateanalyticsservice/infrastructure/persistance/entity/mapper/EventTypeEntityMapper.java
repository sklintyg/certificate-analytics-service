package se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.mapper;

import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.EventTypeEntity;

public class EventTypeEntityMapper {

  private EventTypeEntityMapper() {
    throw new IllegalStateException("Utility class");
  }
  
  public static EventTypeEntity map(String eventType) {
    return EventTypeEntity.builder()
        .eventType(eventType)
        .build();
  }
}
