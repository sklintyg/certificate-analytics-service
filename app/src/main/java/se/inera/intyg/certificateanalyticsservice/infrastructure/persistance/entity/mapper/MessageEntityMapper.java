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

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateanalyticsservice.application.messages.model.PseudonymizedAnalyticsMessage;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.MessageEntity;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository.MessageEntityRepository;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository.PartyRepository;

@Component
@RequiredArgsConstructor
public class MessageEntityMapper {

  private final MessageEntityRepository messageEntityRepository;
  private final PartyRepository partyRepository;

  public MessageEntity map(PseudonymizedAnalyticsMessage pseudonymizedAnalyticsMessage) {
    if (missingMessage(pseudonymizedAnalyticsMessage)) {
      return null;
    }

    return messageEntityRepository
        .findByMessageId(pseudonymizedAnalyticsMessage.getMessageId())
        .orElseGet(() -> createAndSave(pseudonymizedAnalyticsMessage));
  }

  private static boolean missingMessage(
      PseudonymizedAnalyticsMessage pseudonymizedAnalyticsMessage) {
    return pseudonymizedAnalyticsMessage.getMessageId() == null
        || pseudonymizedAnalyticsMessage.getMessageId().isBlank();
  }

  private MessageEntity createAndSave(PseudonymizedAnalyticsMessage pseudonymizedAnalyticsMessage) {
    final var questionIds = pseudonymizedAnalyticsMessage.getMessageQuestionIds();
    return messageEntityRepository.save(
        MessageEntity.builder()
            .messageId(pseudonymizedAnalyticsMessage.getMessageId())
            .messageType(pseudonymizedAnalyticsMessage.getMessageType())
            .messageAnswerId(pseudonymizedAnalyticsMessage.getMessageAnswerId())
            .messageReminderId(pseudonymizedAnalyticsMessage.getMessageReminderId())
            .sent(pseudonymizedAnalyticsMessage.getMessageSent())
            .lastDateToAnswer(pseudonymizedAnalyticsMessage.getMessageLastDateToAnswer())
            .complementFirstQuestionId(getQuestionId(questionIds, 0))
            .complementSecondQuestionId(getQuestionId(questionIds, 1))
            .complementThirdQuestionId(getQuestionId(questionIds, 2))
            .complementFourthQuestionId(getQuestionId(questionIds, 3))
            .complementFifthQuestionId(getQuestionId(questionIds, 4))
            .complementSixthQuestionId(getQuestionId(questionIds, 5))
            .complementSeventhQuestionId(getQuestionId(questionIds, 6))
            .build());
  }

  private static String getQuestionId(List<String> questionIds, int index) {
    if (questionIds == null || questionIds.size() <= index) {
      return null;
    }
    return questionIds.get(index);
  }
}
