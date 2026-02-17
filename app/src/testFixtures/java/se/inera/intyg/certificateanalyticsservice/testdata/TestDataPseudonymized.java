package se.inera.intyg.certificateanalyticsservice.testdata;

import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.CARE_PROVIDER_ID;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.CERTIFICATE_PARENT_TYPE;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.CERTIFICATE_TYPE;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.CERTIFICATE_TYPE_VERSION;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.EVENT_TIMESTAMP;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.EVENT_TYPE_CERTIFICATE_SENT;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.EVENT_TYPE_COMPLEMENT_FROM_RECIPIENT;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.EVENT_TYPE_DRAFT_CREATED;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.HASHED_CERTIFICATE_ID;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.HASHED_CERTIFICATE_PARENT_ID;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.HASHED_ID;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.HASHED_PATIENT_ID;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.HASHED_PRIVATE_PRACTITIONER_CARE_PROVIDER_ID;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.HASHED_PRIVATE_PRACTITIONER_UNIT_ID;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.HASHED_SESSION_ID;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.HASHED_USER_ID;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.ORIGIN;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.RECIPIENT;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.ROLE;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.UNIT_ID;

import java.time.LocalDateTime;
import java.util.List;
import se.inera.intyg.certificateanalyticsservice.application.messages.model.PseudonymizedAnalyticsMessage;
import se.inera.intyg.certificateanalyticsservice.application.messages.model.PseudonymizedAnalyticsMessage.PseudonymizedAnalyticsMessageBuilder;

public class TestDataPseudonymized {

  private TestDataPseudonymized() {
    throw new IllegalStateException("Utility class");
  }

  public static PseudonymizedAnalyticsMessageBuilder draftPrivatePractitionerPseudonymizedMessageBuilder() {
    return draftPseudonymizedMessageBuilder()
        .eventUnitId(HASHED_PRIVATE_PRACTITIONER_UNIT_ID)
        .eventCareProviderId(HASHED_PRIVATE_PRACTITIONER_CARE_PROVIDER_ID)
        .certificateUnitId(HASHED_PRIVATE_PRACTITIONER_UNIT_ID)
        .certificateCareProviderId(HASHED_PRIVATE_PRACTITIONER_CARE_PROVIDER_ID);
  }

  public static PseudonymizedAnalyticsMessageBuilder draftPseudonymizedMessageBuilder() {
    return PseudonymizedAnalyticsMessage.builder()
        .id(HASHED_ID)
        .eventTimestamp(LocalDateTime.parse(EVENT_TIMESTAMP))
        .eventMessageType(EVENT_TYPE_DRAFT_CREATED)
        .eventUserId(HASHED_USER_ID)
        .eventRole(ROLE)
        .eventUnitId(UNIT_ID)
        .eventCareProviderId(CARE_PROVIDER_ID)
        .eventOrigin(ORIGIN)
        .eventSessionId(HASHED_SESSION_ID)
        .certificateId(HASHED_CERTIFICATE_ID)
        .certificateType(CERTIFICATE_TYPE)
        .certificateTypeVersion(CERTIFICATE_TYPE_VERSION)
        .certificatePatientId(HASHED_PATIENT_ID)
        .certificateUnitId(UNIT_ID)
        .certificateCareProviderId(CARE_PROVIDER_ID);
  }

  public static PseudonymizedAnalyticsMessageBuilder sentPseudonymizedMessageBuilder() {
    return PseudonymizedAnalyticsMessage.builder()
        .id(HASHED_ID)
        .eventTimestamp(LocalDateTime.parse(EVENT_TIMESTAMP))
        .eventMessageType(EVENT_TYPE_CERTIFICATE_SENT)
        .eventUserId(HASHED_USER_ID)
        .eventRole(ROLE)
        .eventUnitId(UNIT_ID)
        .eventCareProviderId(CARE_PROVIDER_ID)
        .eventOrigin(ORIGIN)
        .eventSessionId(HASHED_SESSION_ID)
        .certificateId(HASHED_CERTIFICATE_ID)
        .certificateType(CERTIFICATE_TYPE)
        .certificateTypeVersion(CERTIFICATE_TYPE_VERSION)
        .certificatePatientId(HASHED_PATIENT_ID)
        .certificateUnitId(UNIT_ID)
        .certificateCareProviderId(CARE_PROVIDER_ID)
        .certificateRelationParentId(HASHED_CERTIFICATE_PARENT_ID)
        .certificateRelationParentType(CERTIFICATE_PARENT_TYPE)
        .recipientId(RECIPIENT);
  }

  public static PseudonymizedAnalyticsMessageBuilder messagePseudonymizedMessageBuilder() {
    return PseudonymizedAnalyticsMessage.builder()
        .id(TestDataConstants.HASHED_ID)
        .eventTimestamp(LocalDateTime.parse(EVENT_TIMESTAMP))
        .eventMessageType(EVENT_TYPE_COMPLEMENT_FROM_RECIPIENT)
        .eventUserId(HASHED_USER_ID)
        .eventRole(ROLE)
        .eventUnitId(UNIT_ID)
        .eventCareProviderId(CARE_PROVIDER_ID)
        .eventOrigin(ORIGIN)
        .eventSessionId(HASHED_SESSION_ID)
        .certificateId(HASHED_CERTIFICATE_ID)
        .certificateType(CERTIFICATE_TYPE)
        .certificateTypeVersion(CERTIFICATE_TYPE_VERSION)
        .certificatePatientId(HASHED_PATIENT_ID)
        .certificateUnitId(UNIT_ID)
        .certificateCareProviderId(CARE_PROVIDER_ID)
        .messageId(TestDataConstants.HASHED_MESSAGE_ID)
        .messageAnswerId(TestDataConstants.HASHED_MESSAGE_ANSWER_ID)
        .messageReminderId(TestDataConstants.HASHED_MESSAGE_REMINDER_ID)
        .messageType(TestDataConstants.MESSAGE_TYPE)
        .messageSent(TestDataConstants.MESSAGE_SENT)
        .messageLastDateToAnswer(TestDataConstants.MESSAGE_LAST_DATE_TO_ANSWER)
        .messageQuestionIds(List.of(
            TestDataConstants.MESSAGE_QUESTION_ID_1,
            TestDataConstants.MESSAGE_QUESTION_ID_2)
        )
        .messageSenderId(TestDataConstants.MESSAGE_SENDER)
        .messageRecipientId(TestDataConstants.MESSAGE_RECIPIENT);
  }
}
