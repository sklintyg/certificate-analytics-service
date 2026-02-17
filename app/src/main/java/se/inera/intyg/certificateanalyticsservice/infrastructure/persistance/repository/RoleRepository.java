package se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.RoleEntity;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.mapper.RoleEntityMapper;

@Repository
@RequiredArgsConstructor
public class RoleRepository {

  private final RoleEntityRepository roleEntityRepository;

  public RoleEntity findOrCreate(String role) {
    if (role == null || role.isBlank()) {
      return null;
    }
    return roleEntityRepository.findByRole(role)
        .orElseGet(() -> roleEntityRepository.save(RoleEntityMapper.map(role)));
  }
}
