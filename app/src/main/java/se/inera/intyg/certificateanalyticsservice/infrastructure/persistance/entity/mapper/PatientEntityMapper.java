package se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.mapper;

import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.PatientEntity;

public class PatientEntityMapper {

  private PatientEntityMapper() {
    throw new IllegalStateException("Utility class");
  }

  public static PatientEntity map(String patientId) {
    return PatientEntity.builder()
        .patientId(patientId)
        .build();
  }
}
