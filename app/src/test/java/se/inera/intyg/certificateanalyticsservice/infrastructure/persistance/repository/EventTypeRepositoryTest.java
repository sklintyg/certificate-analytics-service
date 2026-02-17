package se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.EventTypeEntity;
import se.inera.intyg.certificateanalyticsservice.testdata.TestDataEntities;

@ExtendWith(MockitoExtension.class)
class EventTypeRepositoryTest {

  @InjectMocks
  private EventTypeRepository eventTypeRepository;
  @Mock
  private EventTypeEntityRepository eventTypeEntityRepository;

  @Test
  void shouldCreateNewEventTypeEntityIfNotExists() {
    final var eventType = TestDataEntities.eventTypeEntity();
    final var savedEventType = mock(EventTypeEntity.class);
    when(eventTypeEntityRepository.findByEventType(eventType.getEventType())).thenReturn(
        Optional.empty());
    when(eventTypeEntityRepository.save(eventType)).thenReturn(savedEventType);

    final var result = eventTypeRepository.findOrCreate(eventType.getEventType());

    assertEquals(savedEventType, result);
  }

  @Test
  void shouldFindExistingEventTypeEntity() {
    final var eventTypeName = TestDataEntities.eventTypeEntity().getEventType();
    final var entity = mock(EventTypeEntity.class);
    when(eventTypeEntityRepository.findByEventType(eventTypeName)).thenReturn(Optional.of(entity));

    final var result = eventTypeRepository.findOrCreate(eventTypeName);

    assertEquals(entity, result);
  }
}

