package se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.CareProviderEntity;
import se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants;

class CareProviderEntityMapperTest {

  @Test
  void shouldMapCareProviderIdCorrectly() {
    final var expected = CareProviderEntity.builder()
        .hsaId(TestDataConstants.CARE_PROVIDER_ID)
        .build();
    final var result = CareProviderEntityMapper.map(TestDataConstants.CARE_PROVIDER_ID);
    assertEquals(expected, result);
  }
}

