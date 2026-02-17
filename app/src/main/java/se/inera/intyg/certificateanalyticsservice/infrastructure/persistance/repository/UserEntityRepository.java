package se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.UserEntity;

@Repository
public interface UserEntityRepository extends CrudRepository<UserEntity, Long> {

  Optional<UserEntity> findByUserId(String userId);
}

