package se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.PartyEntity;
import se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants;

class PartyEntityMapperTest {

  @Test
  void shouldMapPartyCorrectly() {
    final var expected = PartyEntity.builder()
        .party(TestDataConstants.RECIPIENT)
        .build();

    final var result = PartyEntityMapper.map(TestDataConstants.RECIPIENT);

    assertEquals(expected, result);
  }
}