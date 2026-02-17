package se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.OriginEntity;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.mapper.OriginEntityMapper;

@Repository
@RequiredArgsConstructor
public class OriginRepository {

  private final OriginEntityRepository originEntityRepository;

  public OriginEntity findOrCreate(String origin) {
    if (origin == null || origin.isBlank()) {
      return null;
    }
    return originEntityRepository.findByOrigin(origin)
        .orElseGet(() -> originEntityRepository.save(OriginEntityMapper.map(origin)));
  }
}
