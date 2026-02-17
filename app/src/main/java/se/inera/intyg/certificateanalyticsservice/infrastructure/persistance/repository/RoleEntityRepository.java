package se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.RoleEntity;

@Repository
public interface RoleEntityRepository extends CrudRepository<RoleEntity, Long> {

  Optional<RoleEntity> findByRole(String role);
}

