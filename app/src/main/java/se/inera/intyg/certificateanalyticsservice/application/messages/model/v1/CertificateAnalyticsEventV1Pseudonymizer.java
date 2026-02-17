package se.inera.intyg.certificateanalyticsservice.application.messages.model.v1;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateanalyticsservice.application.messages.model.AnalyticsMessagePseudonymizer;
import se.inera.intyg.certificateanalyticsservice.application.messages.model.CertificateAnalyticsMessage;
import se.inera.intyg.certificateanalyticsservice.application.messages.model.PseudonymizedAnalyticsMessage;
import se.inera.intyg.certificateanalyticsservice.infrastructure.pseudonymization.PseudonymizationTokenGenerator;

@Component
@RequiredArgsConstructor
public class CertificateAnalyticsEventV1Pseudonymizer implements AnalyticsMessagePseudonymizer {

  private final PseudonymizationTokenGenerator pseudonymizationTokenGenerator;

  @Override
  public boolean canPseudonymize(CertificateAnalyticsMessage message) {
    return message instanceof CertificateAnalyticsMessageV1;
  }

  @Override
  public PseudonymizedAnalyticsMessage pseudonymize(CertificateAnalyticsMessage message) {
    if (!(message instanceof CertificateAnalyticsMessageV1 messageV1)) {
      throw new IllegalArgumentException("Invalid message type");
    }

    final var event = messageV1.getEvent();
    final var certificate = messageV1.getCertificate();
    return PseudonymizedAnalyticsMessage.builder()
        .id(id(messageV1))
        .eventTimestamp(event.getTimestamp())
        .eventMessageType(event.getMessageType())
        .eventUserId(eventUserId(event))
        .eventRole(event.getRole())
        .eventUnitId(eventUnitId(event))
        .eventCareProviderId(eventCareProviderId(event))
        .eventOrigin(event.getOrigin())
        .eventSessionId(eventSessionId(event))
        .certificateId(certificateId(certificate))
        .certificateType(certificate.getType())
        .certificateTypeVersion(certificate.getTypeVersion())
        .certificatePatientId(certificatePatientId(certificate))
        .certificateUnitId(certificateUnitId(certificate))
        .certificateCareProviderId(certificateCareProviderId(certificate))
        .certificateRelationParentId(certificateRelationParentId(certificate))
        .certificateRelationParentType(certificateRelationParentType(certificate))
        .recipientId(recipientId(messageV1))
        .messageId(messageId(messageV1))
        .messageAnswerId(messageAnswerId(messageV1))
        .messageReminderId(messageReminderId(messageV1))
        .messageType(messageType(messageV1))
        .messageSent(messageSent(messageV1))
        .messageLastDateToAnswer(messageLastDateToAnswer(messageV1))
        .messageQuestionIds(messageQuestionIds(messageV1))
        .messageSenderId(messageSenderId(messageV1))
        .messageRecipientId(messageRecipientId(messageV1))
        .build();
  }

  private String id(CertificateAnalyticsMessageV1 messageV1) {
    return pseudonymizationTokenGenerator.id(messageV1.getMessageId());
  }

  private String eventUserId(CertificateAnalyticsEventV1 event) {
    return pseudonymizationTokenGenerator.staffId(event.getUserId());
  }

  private String eventUnitId(CertificateAnalyticsEventV1 event) {
    return isPrivatePractitioner(event.getUnitId()) ?
        pseudonymizationTokenGenerator.eventUnitId(event.getUnitId()) :
        event.getUnitId();
  }

  private String eventCareProviderId(CertificateAnalyticsEventV1 event) {
    return isPrivatePractitioner(event.getCareProviderId()) ?
        pseudonymizationTokenGenerator.eventCareProviderId(event.getCareProviderId()) :
        event.getCareProviderId();
  }

  private String eventSessionId(CertificateAnalyticsEventV1 event) {
    return pseudonymizationTokenGenerator.sessionId(event.getSessionId());
  }

  private String certificateId(CertificateAnalyticsEventCertificateV1 certificate) {
    return pseudonymizationTokenGenerator.certificateId(certificate.getId());
  }

  private String certificatePatientId(CertificateAnalyticsEventCertificateV1 certificate) {
    return pseudonymizationTokenGenerator.patientId(certificate.getPatientId());
  }

  private String certificateUnitId(CertificateAnalyticsEventCertificateV1 certificate) {
    return isPrivatePractitioner(certificate.getUnitId()) ?
        pseudonymizationTokenGenerator.certificateUnitId(certificate.getUnitId()) :
        certificate.getUnitId();
  }

  private String certificateCareProviderId(CertificateAnalyticsEventCertificateV1 certificate) {
    return isPrivatePractitioner(certificate.getCareProviderId()) ?
        pseudonymizationTokenGenerator.certificateCareProviderId(certificate.getCareProviderId()) :
        certificate.getCareProviderId();
  }

  private String certificateRelationParentId(
      CertificateAnalyticsEventCertificateV1 certificate) {
    return missingParent(certificate) ? null :
        pseudonymizationTokenGenerator.parentCertificateId(certificate.getParent().getId());
  }

  private String certificateRelationParentType(
      CertificateAnalyticsEventCertificateV1 certificate) {
    return missingParent(certificate) ? null :
        certificate.getParent().getType();
  }

  private String recipientId(CertificateAnalyticsMessageV1 messageV1) {
    return missingRecipient(messageV1) ? null :
        messageV1.getRecipient().getId();
  }

  private String messageId(CertificateAnalyticsMessageV1 messageV1) {
    return missingMessage(messageV1) ? null :
        pseudonymizationTokenGenerator.messageId(messageV1.getMessage().getId());
  }

  private String messageAnswerId(CertificateAnalyticsMessageV1 messageV1) {
    return missingMessage(messageV1) ? null :
        pseudonymizationTokenGenerator.messageAnswerId(messageV1.getMessage().getAnswerId());
  }

  private String messageReminderId(CertificateAnalyticsMessageV1 messageV1) {
    return missingMessage(messageV1) ? null :
        pseudonymizationTokenGenerator.messageReminderId(messageV1.getMessage().getReminderId());
  }

  private String messageType(CertificateAnalyticsMessageV1 messageV1) {
    return missingMessage(messageV1) ? null :
        messageV1.getMessage().getType();
  }

  private LocalDateTime messageSent(CertificateAnalyticsMessageV1 messageV1) {
    return missingMessage(messageV1) ? null :
        messageV1.getMessage().getSent();
  }

  private LocalDate messageLastDateToAnswer(CertificateAnalyticsMessageV1 messageV1) {
    return missingMessage(messageV1) ? null :
        messageV1.getMessage().getLastDateToAnswer();
  }

  private List<String> messageQuestionIds(CertificateAnalyticsMessageV1 messageV1) {
    return missingMessage(messageV1) ? null :
        messageV1.getMessage().getQuestionIds();
  }

  private String messageSenderId(CertificateAnalyticsMessageV1 messageV1) {
    return missingMessage(messageV1) ? null :
        messageV1.getMessage().getSender();
  }

  private String messageRecipientId(CertificateAnalyticsMessageV1 messageV1) {
    return missingMessage(messageV1) ? null :
        messageV1.getMessage().getRecipient();
  }

  private boolean isPrivatePractitioner(String hsaId) {
    return hsaId != null && hsaId.toUpperCase().contains("-WEBCERT");
  }

  private boolean missingParent(CertificateAnalyticsEventCertificateV1 certificate) {
    return certificate.getParent() == null || certificate.getParent().getId() == null;
  }

  private boolean missingRecipient(CertificateAnalyticsMessageV1 message) {
    return message.getRecipient() == null || message.getRecipient().getId() == null;
  }

  private boolean missingMessage(CertificateAnalyticsMessageV1 message) {
    return message.getMessage() == null || message.getMessage().getId() == null;
  }
}