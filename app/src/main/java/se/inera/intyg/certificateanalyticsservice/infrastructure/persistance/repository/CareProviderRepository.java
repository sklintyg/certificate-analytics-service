package se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.CareProviderEntity;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.mapper.CareProviderEntityMapper;

@Repository
@RequiredArgsConstructor
public class CareProviderRepository {

  private final CareProviderEntityRepository careProviderEntityRepository;

  public CareProviderEntity findOrCreate(String hsaId) {
    if (hsaId == null || hsaId.isBlank()) {
      return null;
    }
    return careProviderEntityRepository.findByHsaId(hsaId)
        .orElseGet(() -> careProviderEntityRepository.save(CareProviderEntityMapper.map(hsaId)));
  }
}
