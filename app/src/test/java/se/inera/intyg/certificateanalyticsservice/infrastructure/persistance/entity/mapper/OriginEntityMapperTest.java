package se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.OriginEntity;
import se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants;

class OriginEntityMapperTest {

  @Test
  void shouldMapOriginCorrectly() {
    final var expected = OriginEntity.builder()
        .origin(TestDataConstants.ORIGIN)
        .build();
    final var result = OriginEntityMapper.map(TestDataConstants.ORIGIN);
    assertEquals(expected, result);
  }
}

