package se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.EventTypeEntity;

class EventTypeEntityMapperTest {

  @Test
  void shouldMapEventTypeCorrectly() {
    final var expected = EventTypeEntity.builder()
        .eventType("CREATED")
        .build();

    final var result = EventTypeEntityMapper.map("CREATED");

    assertEquals(expected, result);
  }
}

