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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.MESSAGE_ID;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataPseudonymized.draftPseudonymizedMessageBuilder;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataPseudonymized.sentPseudonymizedMessageBuilder;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.EventEntity;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.mapper.EventMapper;
import se.inera.intyg.certificateanalyticsservice.testdata.TestDataEntities;

@ExtendWith(MockitoExtension.class)
class JpaCertificateAnalyticsEventRepositoryTest {

  @Mock private EventEntityRepository eventEntityRepository;
  @Mock private EventMapper eventMapper;
  @InjectMocks private JpaAnalyticsEventRepository jpaCertificateAnalyticsEventRepository;

  @Test
  void shouldMapAndSaveCreatedEventMessage() {
    final var message = draftPseudonymizedMessageBuilder().build();
    final var entityToSave = mock(EventEntity.class);
    when(eventMapper.toEntity(message)).thenReturn(entityToSave);
    when(eventEntityRepository.save(entityToSave)).thenReturn(entityToSave);

    jpaCertificateAnalyticsEventRepository.save(message);

    verify(eventEntityRepository).save(entityToSave);
  }

  @Test
  void shouldReturnPseudonymizedMessageIfExists() {
    final var excepted = sentPseudonymizedMessageBuilder().build();

    final var entity = TestDataEntities.sentEventEntityBuilder().build();
    when(eventEntityRepository.findByMessageId(MESSAGE_ID)).thenReturn(Optional.of(entity));
    when(eventMapper.toDomain(entity)).thenReturn(excepted);

    final var actual = jpaCertificateAnalyticsEventRepository.findByMessageId(MESSAGE_ID);

    assertEquals(excepted, actual);
  }

  @Test
  void shouldNotAllowClear() {
    assertThrows(
        UnsupportedOperationException.class, () -> jpaCertificateAnalyticsEventRepository.clear());
  }
}
