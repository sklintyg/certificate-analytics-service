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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.CareProviderEntity;
import se.inera.intyg.certificateanalyticsservice.testdata.TestDataEntities;

@ExtendWith(MockitoExtension.class)
class CareProviderRepositoryTest {

  @InjectMocks private CareProviderRepository careProviderRepository;
  @Mock private CareProviderEntityRepository careProviderEntityRepository;

  @Test
  void shouldCreateNewCareProviderEntityIfNotExists() {
    final var careProvider = TestDataEntities.careProviderEntity();
    final var savedCareProvider = mock(CareProviderEntity.class);
    when(careProviderEntityRepository.findByHsaId(careProvider.getHsaId()))
        .thenReturn(Optional.empty());
    when(careProviderEntityRepository.save(careProvider)).thenReturn(savedCareProvider);

    final var result = careProviderRepository.findOrCreate(careProvider.getHsaId());

    assertEquals(savedCareProvider, result);
  }

  @Test
  void shouldFindExistingCareProviderEntity() {
    final var hsaId = TestDataEntities.careProviderEntity().getHsaId();
    final var entity = mock(CareProviderEntity.class);
    when(careProviderEntityRepository.findByHsaId(hsaId)).thenReturn(Optional.of(entity));

    final var result = careProviderRepository.findOrCreate(hsaId);

    assertEquals(entity, result);
  }

  @Test
  void shouldReturnNullIfHsaIdIsNull() {
    assertNull(careProviderRepository.findOrCreate(null));
  }

  @Test
  void shouldReturnNullIfHsaIdIsEmpty() {
    assertNull(careProviderRepository.findOrCreate(" "));
  }
}
