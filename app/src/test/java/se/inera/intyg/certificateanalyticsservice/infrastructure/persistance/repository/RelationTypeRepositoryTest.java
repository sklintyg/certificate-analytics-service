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
import static org.mockito.Mockito.when;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.CERTIFICATE_PARENT_TYPE;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataEntities.relationTypeEntityBuilder;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RelationTypeRepositoryTest {

  @Mock private RelationTypeEntityRepository relationTypeEntityRepository;
  @InjectMocks private RelationTypeRepository relationTypeRepository;

  @Test
  void shouldCreateNewRelationTypeEntityIfNotExists() {
    final var expected = relationTypeEntityBuilder().key(99L).build();

    final var relationTypeEntity = relationTypeEntityBuilder().build();

    when(relationTypeEntityRepository.findByRelationType(CERTIFICATE_PARENT_TYPE))
        .thenReturn(Optional.empty());
    when(relationTypeEntityRepository.save(relationTypeEntity)).thenReturn(expected);

    final var actual = relationTypeRepository.findOrCreate(CERTIFICATE_PARENT_TYPE);

    assertEquals(expected, actual);
  }

  @Test
  void shouldFindExistingRelationTypeEntity() {
    final var expected = relationTypeEntityBuilder().key(99L).build();

    when(relationTypeEntityRepository.findByRelationType(CERTIFICATE_PARENT_TYPE))
        .thenReturn(Optional.of(expected));

    final var actual = relationTypeRepository.findOrCreate(CERTIFICATE_PARENT_TYPE);

    assertEquals(expected, actual);
  }

  @Test
  void shouldReturnNullIfRelationTypeIsNull() {
    assertNull(relationTypeRepository.findOrCreate(null));
  }

  @Test
  void shouldReturnNullIfRelationTypeIsEmpty() {
    assertNull(relationTypeRepository.findOrCreate(" "));
  }
}
