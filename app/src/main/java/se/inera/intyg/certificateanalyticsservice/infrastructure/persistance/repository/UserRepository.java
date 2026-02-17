package se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.UserEntity;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.mapper.UserEntityMapper;

@Repository
@RequiredArgsConstructor
public class UserRepository {

  private final UserEntityRepository userEntityRepository;

  public UserEntity findOrCreate(String userId) {
    if (userId == null || userId.isBlank()) {
      return null;
    }
    return userEntityRepository.findByUserId(userId)
        .orElseGet(() -> userEntityRepository.save(UserEntityMapper.map(userId)));
  }
}
