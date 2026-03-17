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
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.PartyEntity;
import se.inera.intyg.certificateanalyticsservice.testdata.TestDataEntities;

@ExtendWith(MockitoExtension.class)
class PartyRepositoryTest {

  @InjectMocks private PartyRepository partyRepository;
  @Mock private PartyEntityRepository partyEntityRepository;

  @Test
  void shouldCreateNewRecipientEntityIfNotExists() {
    final var recipient = TestDataEntities.recipientPartyEntity();
    final var savedRecipient = mock(PartyEntity.class);
    when(partyEntityRepository.findByParty(recipient.getParty())).thenReturn(Optional.empty());
    when(partyEntityRepository.save(recipient)).thenReturn(savedRecipient);

    final var result = partyRepository.findOrCreate(recipient.getParty());

    assertEquals(savedRecipient, result);
  }

  @Test
  void shouldFindExistingRecipientEntity() {
    final var recipientName = TestDataEntities.recipientPartyEntity().getParty();
    final var entity = mock(PartyEntity.class);
    when(partyEntityRepository.findByParty(recipientName)).thenReturn(Optional.of(entity));

    final var result = partyRepository.findOrCreate(recipientName);

    assertEquals(entity, result);
  }

  @Test
  void shouldReturnNullIfRecipientIsNull() {
    assertNull(partyRepository.findOrCreate(null));
  }

  @Test
  void shouldReturnNullIfRecipientIsEmpty() {
    assertNull(partyRepository.findOrCreate(" "));
  }
}
