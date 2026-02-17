package se.inera.intyg.certificateanalyticsservice.application.messages.model.v1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.PRIVATE_PRACTITIONER_CARE_PROVIDER_ID;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.PRIVATE_PRACTITIONER_UNIT_ID;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataMessages.draftCertificateBuilder;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataMessages.draftEventBuilder;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataMessages.draftMessageBuilder;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataMessages.receivedQuestionMessageBuilder;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataMessages.replaceMessageBuilder;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataMessages.sentCertificateBuilder;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataMessages.sentMessageBuilder;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateanalyticsservice.application.messages.model.CertificateAnalyticsMessage;
import se.inera.intyg.certificateanalyticsservice.infrastructure.pseudonymization.PseudonymizationTokenGenerator;

@ExtendWith(MockitoExtension.class)
class CertificateAnalyticsEventV1PseudonymizerTest {

  @Mock
  private PseudonymizationTokenGenerator pseudonymizationTokenGenerator;

  @InjectMocks
  private CertificateAnalyticsEventV1Pseudonymizer certificateAnalyticsEventV1Pseudonymizer;

  @Test
  void shallReturnTrueCanPseudonymizeCertificateAnalyticEventV1() {
    final var expected = true;
    final var message = draftMessageBuilder().build();

    final var actual = certificateAnalyticsEventV1Pseudonymizer.canPseudonymize(message);

    assertEquals(expected, actual);
  }

  @Test
  void shallReturnFalseCanPseudonymizeUnknownMessage() {
    final var expected = false;
    final var message = mock(CertificateAnalyticsMessage.class);

    final var actual = certificateAnalyticsEventV1Pseudonymizer.canPseudonymize(message);

    assertEquals(expected, actual);
  }

  @Test
  void shallThrowIfTryingToPseudonymizeUnknownMessage() {
    final var message = mock(CertificateAnalyticsMessage.class);

    assertThrows(IllegalArgumentException.class, () ->
        certificateAnalyticsEventV1Pseudonymizer.pseudonymize(message)
    );
  }

  @Test
  void shallReturnPseudonymizedId() {
    final var expected = "pseudonymized-id";
    final var message = draftMessageBuilder().build();

    when(pseudonymizationTokenGenerator.id(message.getMessageId()))
        .thenReturn(expected);

    final var actual = certificateAnalyticsEventV1Pseudonymizer.pseudonymize(message);

    assertEquals(expected, actual.getId());
  }

  @Test
  void shallReturnPseudonymizedCertificateId() {
    final var expected = "pseudonymized-certificate-id";
    final var message = draftMessageBuilder().build();

    when(pseudonymizationTokenGenerator.certificateId(message.getCertificate().getId()))
        .thenReturn(expected);

    final var actual = certificateAnalyticsEventV1Pseudonymizer.pseudonymize(message);

    assertEquals(expected, actual.getCertificateId());
  }

  @Test
  void shallReturnCertificateType() {
    final var message = draftMessageBuilder().build();
    final var expected = message.getCertificate().getType();

    final var actual = certificateAnalyticsEventV1Pseudonymizer.pseudonymize(message);

    assertEquals(expected, actual.getCertificateType());
  }

  @Test
  void shallReturnCertificateTypeVersion() {
    final var message = draftMessageBuilder().build();
    final var expected = message.getCertificate().getTypeVersion();

    final var actual = certificateAnalyticsEventV1Pseudonymizer.pseudonymize(message);

    assertEquals(expected, actual.getCertificateTypeVersion());
  }

  @Test
  void shallReturnPseudonymizedCertificatePatientId() {
    final var expected = "pseudonymized-patient-id";
    final var message = draftMessageBuilder().build();

    when(pseudonymizationTokenGenerator.patientId(message.getCertificate().getPatientId()))
        .thenReturn(expected);

    final var actual = certificateAnalyticsEventV1Pseudonymizer.pseudonymize(message);

    assertEquals(expected, actual.getCertificatePatientId());
  }

  @Test
  void shallReturnCertificateUnitId() {
    final var message = draftMessageBuilder().build();
    final var expected = message.getCertificate().getUnitId();

    final var actual = certificateAnalyticsEventV1Pseudonymizer.pseudonymize(message);

    assertEquals(expected, actual.getCertificateUnitId());
  }

  @Test
  void shallReturnPseudonymizedCertificateUnitIdForPrivatePractitioners() {
    final var expected = "pseudonymized-certificate-unit-id";
    final var message = draftMessageBuilder()
        .certificate(
            draftCertificateBuilder()
                .unitId(PRIVATE_PRACTITIONER_UNIT_ID)
                .build()
        )
        .build();

    when(pseudonymizationTokenGenerator.certificateUnitId(message.getCertificate().getUnitId()))
        .thenReturn(expected);

    final var actual = certificateAnalyticsEventV1Pseudonymizer.pseudonymize(message);

    assertEquals(expected, actual.getCertificateUnitId());
  }

  @Test
  void shallReturnCertificateCareProviderId() {
    final var message = draftMessageBuilder().build();
    final var expected = message.getCertificate().getCareProviderId();

    final var actual = certificateAnalyticsEventV1Pseudonymizer.pseudonymize(message);

    assertEquals(expected, actual.getCertificateCareProviderId());
  }

  @Test
  void shallReturnPseudonymizedCertificateCareProviderIdForPrivatePractitioners() {
    final var expected = "pseudonymized-certificate-care-provider-id";
    final var message = draftMessageBuilder()
        .certificate(
            draftCertificateBuilder()
                .careProviderId(PRIVATE_PRACTITIONER_CARE_PROVIDER_ID)
                .build()
        )
        .build();

    when(pseudonymizationTokenGenerator.certificateCareProviderId(
        message.getCertificate().getCareProviderId()))
        .thenReturn(expected);

    final var actual = certificateAnalyticsEventV1Pseudonymizer.pseudonymize(message);

    assertEquals(expected, actual.getCertificateCareProviderId());
  }

  @Test
  void shallReturnPseudonymizedCertificateRelationParentIdAsNullIfMissing() {
    final var message = replaceMessageBuilder()
        .certificate(
            sentCertificateBuilder()
                .parent(null)
                .build()
        )
        .build();

    final var actual = certificateAnalyticsEventV1Pseudonymizer.pseudonymize(message);

    assertNull(actual.getCertificateRelationParentId());
  }

  @Test
  void shallReturnPseudonymizedCertificateRelationParentId() {
    final var expected = "pseudonymized-certificate-id";
    final var message = replaceMessageBuilder().build();

    when(pseudonymizationTokenGenerator.parentCertificateId(
        message.getCertificate().getParent().getId()))
        .thenReturn(expected);

    final var actual = certificateAnalyticsEventV1Pseudonymizer.pseudonymize(message);

    assertEquals(expected, actual.getCertificateRelationParentId());
  }

  @Test
  void shallReturnPseudonymizedCertificateRelationParentTypeAsNullIfMissing() {
    final var message = replaceMessageBuilder()
        .certificate(
            sentCertificateBuilder()
                .parent(null)
                .build()
        )
        .build();

    final var actual = certificateAnalyticsEventV1Pseudonymizer.pseudonymize(message);

    assertNull(actual.getCertificateRelationParentType());
  }

  @Test
  void shallReturnPseudonymizedCertificateRelationParentType() {
    final var message = replaceMessageBuilder().build();
    final var expected = message.getCertificate().getParent().getType();

    final var actual = certificateAnalyticsEventV1Pseudonymizer.pseudonymize(message);

    assertEquals(expected, actual.getCertificateRelationParentType());
  }

  @Test
  void shallReturnEventTimeStamp() {
    final var message = draftMessageBuilder().build();
    final var expected = message.getEvent().getTimestamp();

    final var actual = certificateAnalyticsEventV1Pseudonymizer.pseudonymize(message);

    assertEquals(expected, actual.getEventTimestamp());
  }

  @Test
  void shallReturnEventMessageType() {
    final var message = draftMessageBuilder().build();
    final var expected = message.getEvent().getMessageType();

    final var actual = certificateAnalyticsEventV1Pseudonymizer.pseudonymize(message);

    assertEquals(expected, actual.getEventMessageType());
  }

  @Test
  void shallReturnPseudonymizedEventSessionId() {
    final var expected = "pseudonymized-session-id";
    final var message = draftMessageBuilder().build();

    when(pseudonymizationTokenGenerator.sessionId(message.getEvent().getSessionId()))
        .thenReturn(expected);

    final var actual = certificateAnalyticsEventV1Pseudonymizer.pseudonymize(message);

    assertEquals(expected, actual.getEventSessionId());
  }

  @Test
  void shallReturnPseudonymizedEventStaffId() {
    final var expected = "pseudonymized-staff-id";
    final var message = draftMessageBuilder().build();

    when(pseudonymizationTokenGenerator.staffId(message.getEvent().getUserId()))
        .thenReturn(expected);

    final var actual = certificateAnalyticsEventV1Pseudonymizer.pseudonymize(message);

    assertEquals(expected, actual.getEventUserId());
  }

  @Test
  void shallReturnEventUnitId() {
    final var message = draftMessageBuilder().build();
    final var expected = message.getEvent().getUnitId();

    final var actual = certificateAnalyticsEventV1Pseudonymizer.pseudonymize(message);

    assertEquals(expected, actual.getEventUnitId());
  }

  @Test
  void shallReturnPseudonymizedEventUnitIdForPrivatePractitioners() {
    final var expected = "pseudonymized-event-unit-id";
    final var message = draftMessageBuilder()
        .event(
            draftEventBuilder()
                .unitId(PRIVATE_PRACTITIONER_UNIT_ID)
                .build()
        )
        .build();

    when(pseudonymizationTokenGenerator.eventUnitId(message.getEvent().getUnitId()))
        .thenReturn(expected);

    final var actual = certificateAnalyticsEventV1Pseudonymizer.pseudonymize(message);

    assertEquals(expected, actual.getEventUnitId());
  }

  @Test
  void shallReturnEventCareProviderId() {
    final var message = draftMessageBuilder().build();
    final var expected = message.getEvent().getCareProviderId();

    final var actual = certificateAnalyticsEventV1Pseudonymizer.pseudonymize(message);

    assertEquals(expected, actual.getEventCareProviderId());
  }

  @Test
  void shallReturnPseudonymizedEventCareProviderIdForPrivatePractitioners() {
    final var expected = "pseudonymized-event-care-provider-id";
    final var message = draftMessageBuilder()
        .event(
            draftEventBuilder()
                .careProviderId(PRIVATE_PRACTITIONER_CARE_PROVIDER_ID)
                .build()
        )
        .build();

    when(pseudonymizationTokenGenerator.eventCareProviderId(message.getEvent().getCareProviderId()))
        .thenReturn(expected);

    final var actual = certificateAnalyticsEventV1Pseudonymizer.pseudonymize(message);

    assertEquals(expected, actual.getEventCareProviderId());
  }

  @Test
  void shallReturnEventRole() {
    final var message = draftMessageBuilder().build();
    final var expected = message.getEvent().getRole();

    final var actual = certificateAnalyticsEventV1Pseudonymizer.pseudonymize(message);

    assertEquals(expected, actual.getEventRole());
  }

  @Test
  void shallReturnEventOrigin() {
    final var message = draftMessageBuilder().build();
    final var expected = message.getEvent().getOrigin();

    final var actual = certificateAnalyticsEventV1Pseudonymizer.pseudonymize(message);

    assertEquals(expected, actual.getEventOrigin());
  }

  @Test
  void shallReturnRecipient() {
    final var message = sentMessageBuilder().build();
    final var expected = message.getRecipient().getId();

    final var actual = certificateAnalyticsEventV1Pseudonymizer.pseudonymize(message);

    assertEquals(expected, actual.getRecipientId());
  }

  @Test
  void shallReturnPseudonymizedMessageId() {
    final var expected = "pseudonymized-message-id";
    final var message = receivedQuestionMessageBuilder()
        .build();

    when(pseudonymizationTokenGenerator.messageId(message.getMessage().getId()))
        .thenReturn(expected);

    final var actual = certificateAnalyticsEventV1Pseudonymizer.pseudonymize(message);

    assertEquals(expected, actual.getMessageId());
  }

  @Test
  void shallReturnMessageAnswerId() {
    final var expected = "pseudonymized-message-id";
    final var message = receivedQuestionMessageBuilder()
        .build();

    when(pseudonymizationTokenGenerator.messageAnswerId(message.getMessage().getAnswerId()))
        .thenReturn(expected);

    final var actual = certificateAnalyticsEventV1Pseudonymizer.pseudonymize(message);

    assertEquals(expected, actual.getMessageAnswerId());
  }

  @Test
  void shallReturnMessageReminderId() {
    final var expected = "pseudonymized-message-reminder-id";
    final var message = receivedQuestionMessageBuilder()
        .build();

    when(pseudonymizationTokenGenerator.messageReminderId(message.getMessage().getReminderId()))
        .thenReturn(expected);

    final var actual = certificateAnalyticsEventV1Pseudonymizer.pseudonymize(message);

    assertEquals(expected, actual.getMessageReminderId());
  }

  @Test
  void shallReturnMessageType() {
    final var message = receivedQuestionMessageBuilder().build();
    final var expected = message.getMessage().getType();

    final var actual = certificateAnalyticsEventV1Pseudonymizer.pseudonymize(message);

    assertEquals(expected, actual.getMessageType());
  }

  @Test
  void shallReturnMessageSent() {
    final var message = receivedQuestionMessageBuilder().build();
    final var expected = message.getMessage().getSent();

    final var actual = certificateAnalyticsEventV1Pseudonymizer.pseudonymize(message);

    assertEquals(expected, actual.getMessageSent());
  }

  @Test
  void shallReturnMessageLastDateToAnswer() {
    final var message = receivedQuestionMessageBuilder().build();
    final var expected = message.getMessage().getLastDateToAnswer();

    final var actual = certificateAnalyticsEventV1Pseudonymizer.pseudonymize(message);

    assertEquals(expected, actual.getMessageLastDateToAnswer());
  }

  @Test
  void shallReturnMessageQuestionIds() {
    final var message = receivedQuestionMessageBuilder().build();
    final var expected = message.getMessage().getQuestionIds();

    final var actual = certificateAnalyticsEventV1Pseudonymizer.pseudonymize(message);

    assertEquals(expected, actual.getMessageQuestionIds());
  }

  @Test
  void shallReturnMessageSender() {
    final var message = receivedQuestionMessageBuilder().build();
    final var expected = message.getMessage().getSender();

    final var actual = certificateAnalyticsEventV1Pseudonymizer.pseudonymize(message);

    assertEquals(expected, actual.getMessageSenderId());
  }

  @Test
  void shallReturnMessageRecipient() {
    final var message = receivedQuestionMessageBuilder().build();
    final var expected = message.getMessage().getRecipient();

    final var actual = certificateAnalyticsEventV1Pseudonymizer.pseudonymize(message);

    assertEquals(expected, actual.getMessageRecipientId());
  }
}