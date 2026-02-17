package se.inera.intyg.certificateanalyticsservice.application.messages.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PseudonymizedAnalyticsMessage {

  String id;

  LocalDateTime eventTimestamp;
  String eventMessageType;
  String eventUserId;
  String eventRole;
  String eventUnitId;
  String eventCareProviderId;
  String eventOrigin;
  String eventSessionId;

  String certificateId;
  String certificateType;
  String certificateTypeVersion;
  String certificatePatientId;
  String certificateUnitId;
  String certificateCareProviderId;
  String certificateRelationParentId;
  String certificateRelationParentType;

  String recipientId;

  String messageId;
  String messageAnswerId;
  String messageReminderId;
  String messageType;
  LocalDateTime messageSent;
  LocalDate messageLastDateToAnswer;
  List<String> messageQuestionIds;
  String messageSenderId;
  String messageRecipientId;

}