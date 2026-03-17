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
package se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.CERTIFICATE_TYPE;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.CERTIFICATE_TYPE_VERSION;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataPseudonymized.draftPseudonymizedMessageBuilder;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.CertificateEntity;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository.CertificateEntityRepository;

@ExtendWith(MockitoExtension.class)
class CertificateEntityMapperTest {

  @Mock private CertificateEntityRepository certificateEntityRepository;
  @InjectMocks private CertificateEntityMapper certificateEntityMapper;

  @Test
  void shouldMapCertificateCorrectlyWhenCreatingNewEntity() {
    final var message = draftPseudonymizedMessageBuilder().build();
    final var newEntity =
        CertificateEntity.builder()
            .certificateId(message.getCertificateId())
            .certificateType(CERTIFICATE_TYPE)
            .certificateTypeVersion(CERTIFICATE_TYPE_VERSION)
            .build();

    when(certificateEntityRepository.save(newEntity)).thenReturn(newEntity);

    final var result = certificateEntityMapper.map(message);

    assertEquals(newEntity, result);
  }

  @Test
  void shouldReturnExistingCertificateEntityWhenFound() {
    final var message = draftPseudonymizedMessageBuilder().build();
    final var existingEntity = mock(CertificateEntity.class);

    when(certificateEntityRepository.findByCertificateId(message.getCertificateId()))
        .thenReturn(Optional.of(existingEntity));

    final var result = certificateEntityMapper.map(message);
    assertEquals(existingEntity, result);
  }
}
