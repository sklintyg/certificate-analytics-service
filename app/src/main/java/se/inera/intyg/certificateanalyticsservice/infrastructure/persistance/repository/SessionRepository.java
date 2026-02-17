package se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.SessionEntity;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.mapper.SessionEntityMapper;

@Repository
@RequiredArgsConstructor
public class SessionRepository {

  private final SessionEntityRepository sessionEntityRepository;

  public SessionEntity findOrCreate(String sessionId) {
    if (sessionId == null || sessionId.isBlank()) {
      return null;
    }
    return sessionEntityRepository.findBySessionId(sessionId)
        .orElseGet(() -> sessionEntityRepository.save(SessionEntityMapper.map(sessionId)));
  }
}
