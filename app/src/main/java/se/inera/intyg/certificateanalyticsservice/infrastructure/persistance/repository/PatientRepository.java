package se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.PatientEntity;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.mapper.PatientEntityMapper;

@Repository
@RequiredArgsConstructor
public class PatientRepository {

  private final PatientEntityRepository patientEntityRepository;

  public PatientEntity findOrCreate(String patientId) {
    return patientEntityRepository.findByPatientId(patientId)
        .orElseGet(() -> patientEntityRepository.save(PatientEntityMapper.map(patientId)));
  }
}
