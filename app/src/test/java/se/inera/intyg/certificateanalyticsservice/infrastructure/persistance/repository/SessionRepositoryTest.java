package se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.SessionEntity;
import se.inera.intyg.certificateanalyticsservice.testdata.TestDataEntities;

@ExtendWith(MockitoExtension.class)
class SessionRepositoryTest {

  @InjectMocks
  private SessionRepository sessionRepository;
  @Mock
  private SessionEntityRepository sessionEntityRepository;

  @Test
  void shouldCreateNewSessionEntityIfNotExists() {
    final var session = TestDataEntities.sessionEntity();
    final var savedSession = mock(SessionEntity.class);
    when(sessionEntityRepository.findBySessionId(session.getSessionId())).thenReturn(
        Optional.empty());
    when(sessionEntityRepository.save(session)).thenReturn(savedSession);

    final var result = sessionRepository.findOrCreate(session.getSessionId());

    assertEquals(savedSession, result);
  }

  @Test
  void shouldFindExistingSessionEntity() {
    final var sessionId = TestDataEntities.sessionEntity().getSessionId();
    final var entity = mock(SessionEntity.class);
    when(sessionEntityRepository.findBySessionId(sessionId)).thenReturn(Optional.of(entity));

    final var result = sessionRepository.findOrCreate(sessionId);

    assertEquals(entity, result);
  }

  @Test
  void shouldReturnNullIfSessionIsNull() {
    assertNull(sessionRepository.findOrCreate(null));
  }

  @Test
  void shouldReturnNullIfSessionIsEmpty() {
    assertNull(sessionRepository.findOrCreate(" "));
  }
}

