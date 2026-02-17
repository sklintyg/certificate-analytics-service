package se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.UnitEntity;
import se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants;

class UnitEntityMapperTest {

  @Test
  void shouldMapUnitIdCorrectly() {
    final var expected = UnitEntity.builder()
        .hsaId(TestDataConstants.UNIT_ID)
        .build();

    final var result = UnitEntityMapper.map(TestDataConstants.UNIT_ID);

    assertEquals(expected, result);
  }
}
