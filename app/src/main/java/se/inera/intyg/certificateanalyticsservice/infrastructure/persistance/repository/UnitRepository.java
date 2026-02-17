package se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.UnitEntity;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.mapper.UnitEntityMapper;

@Repository
@RequiredArgsConstructor
public class UnitRepository {

  private final UnitEntityRepository unitEntityRepository;

  public UnitEntity findOrCreate(String hsaId) {
    if (hsaId == null || hsaId.isBlank()) {
      return null;
    }
    return unitEntityRepository.findByHsaId(hsaId)
        .orElseGet(() -> unitEntityRepository.save(UnitEntityMapper.map(hsaId)));
  }
}
