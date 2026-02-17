package se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.PatientEntity;
import se.inera.intyg.certificateanalyticsservice.testdata.TestDataEntities;

@ExtendWith(MockitoExtension.class)
class PatientRepositoryTest {

  @InjectMocks
  private PatientRepository patientRepository;
  @Mock
  private PatientEntityRepository patientEntityRepository;

  @Test
  void shouldCreateNewPatientEntityIfNotExists() {
    final var patient = TestDataEntities.patientEntity();
    final var savedPatient = mock(PatientEntity.class);
    when(patientEntityRepository.findByPatientId(patient.getPatientId())).thenReturn(
        Optional.empty());
    when(patientEntityRepository.save(patient)).thenReturn(savedPatient);

    final var result = patientRepository.findOrCreate(patient.getPatientId());

    assertEquals(savedPatient, result);
  }

  @Test
  void shouldFindExistingPatientEntity() {
    final var patientId = TestDataEntities.patientEntity().getPatientId();
    final var entity = mock(PatientEntity.class);
    when(patientEntityRepository.findByPatientId(patientId)).thenReturn(Optional.of(entity));

    final var result = patientRepository.findOrCreate(patientId);

    assertEquals(entity, result);
  }
}

