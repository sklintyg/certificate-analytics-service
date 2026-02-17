package se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.PatientEntity;

@Repository
public interface PatientEntityRepository extends CrudRepository<PatientEntity, Long> {

  Optional<PatientEntity> findByPatientId(String patientId);
}

