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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.HASHED_MESSAGE_ID;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataEntities.messageEntity;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataPseudonymized.messagePseudonymizedMessageBuilder;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository.MessageEntityRepository;

@ExtendWith(MockitoExtension.class)
class MessageEntityMapperTest {

  @Mock private MessageEntityRepository messageEntityRepository;
  @InjectMocks private MessageEntityMapper messageEntityMapper;

  @Test
  void shallReturnNullIfMessageIdNull() {
    final var actual =
        messageEntityMapper.map(messagePseudonymizedMessageBuilder().messageId(null).build());
    assertNull(actual, "Expected null when no message id");
  }

  @Test
  void shallReturnNullIfMessageIdEmpty() {
    final var actual =
        messageEntityMapper.map(messagePseudonymizedMessageBuilder().messageId("").build());
    assertNull(actual, "Expected null when no message id");
  }

  @Test
  void shallReturnExistingMessageIfAlreadyExists() {
    final var expected = messageEntity().build();

    when(messageEntityRepository.findByMessageId(HASHED_MESSAGE_ID))
        .thenReturn(Optional.of(expected));

    final var actual = messageEntityMapper.map(messagePseudonymizedMessageBuilder().build());
    assertEquals(expected, actual);
  }

  @Test
  void shallReturnNewMessageIfNotExists() {
    final var expected = messageEntity().build();

    when(messageEntityRepository.findByMessageId(HASHED_MESSAGE_ID)).thenReturn(Optional.empty());
    when(messageEntityRepository.save(expected)).thenReturn(expected);

    final var actual = messageEntityMapper.map(messagePseudonymizedMessageBuilder().build());

    assertEquals(expected, actual);
  }
}
