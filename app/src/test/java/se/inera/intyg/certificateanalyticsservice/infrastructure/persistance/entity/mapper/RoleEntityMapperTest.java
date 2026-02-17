package se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.RoleEntity;
import se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants;

class RoleEntityMapperTest {

  @Test
  void shouldMapRoleCorrectly() {
    final var expected = RoleEntity.builder()
        .role(TestDataConstants.ROLE)
        .build();

    final var result = RoleEntityMapper.map(TestDataConstants.ROLE);

    assertEquals(expected, result);
  }
}

