package se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.MESSAGE_TYPE;

import org.junit.jupiter.api.Test;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.MessageTypeEntity;

class MessageTypeEntityMapperTest {

  @Test
  void shallMapMessageType() {
    final var expected = MessageTypeEntity.builder()
        .type(MESSAGE_TYPE)
        .build();

    final var result = MessageTypeEntityMapper.map(MESSAGE_TYPE);

    assertEquals(expected, result);
  }
}