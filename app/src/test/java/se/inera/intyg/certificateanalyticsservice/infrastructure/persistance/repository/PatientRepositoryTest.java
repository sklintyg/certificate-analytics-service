/*
 * Copyright (C) 2026 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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

  @InjectMocks private PatientRepository patientRepository;
  @Mock private PatientEntityRepository patientEntityRepository;

  @Test
  void shouldCreateNewPatientEntityIfNotExists() {
    final var patient = TestDataEntities.patientEntity();
    final var savedPatient = mock(PatientEntity.class);
    when(patientEntityRepository.findByPatientId(patient.getPatientId()))
        .thenReturn(Optional.empty());
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
