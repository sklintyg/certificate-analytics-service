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

    return messageEntityRepository.findByMessageId(pseudonymizedAnalyticsMessage.getMessageId())
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
            .build()
    );
  }

  private static String getQuestionId(List<String> questionIds, int index) {
    if (questionIds == null || questionIds.size() <= index) {
      return null;
    }
    return questionIds.get(index);
  }
}
