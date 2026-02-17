package se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.SessionEntity;
import se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants;

class SessionEntityMapperTest {

  @Test
  void shouldMapSessionIdCorrectly() {
    final var expected = SessionEntity.builder()
        .sessionId(TestDataConstants.SESSION_ID)
        .build();
    final var result = SessionEntityMapper.map(TestDataConstants.SESSION_ID);
    assertEquals(expected, result);
  }
}

