package se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import se.inera.intyg.certificateanalyticsservice.application.messages.model.PseudonymizedAnalyticsMessage;
import se.inera.intyg.certificateanalyticsservice.application.messages.repository.AnalyticsMessageRepository;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.mapper.EventMapper;

@Repository
@RequiredArgsConstructor
public class JpaAnalyticsEventRepository implements AnalyticsMessageRepository {

  private final EventEntityRepository eventEntityRepository;
  private final EventMapper eventMapper;

  public void save(PseudonymizedAnalyticsMessage message) {
    final var entity = eventMapper.toEntity(message);
    eventEntityRepository.save(entity);
  }

  @Override
  @Transactional(readOnly = true)
  public PseudonymizedAnalyticsMessage findByMessageId(String messageId) {
    return eventEntityRepository.findByMessageId(messageId)
        .map(eventMapper::toDomain)
        .orElse(null);
  }

  @Override
  public void clear() {
    throw new UnsupportedOperationException("clear() is only supported in testability profile");
  }

  protected EventEntityRepository getEventEntityRepository() {
    return eventEntityRepository;
  }
}
