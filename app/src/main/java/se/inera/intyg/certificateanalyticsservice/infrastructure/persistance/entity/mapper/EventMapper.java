package se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.mapper;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateanalyticsservice.application.messages.model.PseudonymizedAnalyticsMessage;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.EventEntity;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.MessageEntity;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository.CareProviderRepository;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository.CertificateEntityRepository;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository.EventTypeRepository;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository.OriginRepository;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository.PartyRepository;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository.PatientRepository;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository.RelationTypeRepository;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository.RoleRepository;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository.SessionRepository;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository.UnitRepository;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class EventMapper {

  private final UnitRepository unitRepository;
  private final CareProviderRepository careProviderRepository;
  private final UserRepository userRepository;
  private final SessionRepository sessionRepository;
  private final OriginRepository originRepository;
  private final EventTypeRepository eventTypeRepository;
  private final RoleRepository roleRepository;
  private final CertificateEntityMapper certificateEntityMapper;
  private final PartyRepository partyRepository;
  private final CertificateEntityRepository certificateEntityRepository;
  private final RelationTypeRepository relationTypeRepository;
  private final MessageEntityMapper messageEntityMapper;
  private final PatientRepository patientRepository;

  public EventEntity toEntity(PseudonymizedAnalyticsMessage message) {
    final var certificateEntity = certificateEntityMapper.map(message);
    final var messageEntity = messageEntityMapper.map(message);

    return EventEntity.builder()
        .certificate(certificateEntity)
        .parentRelationCertificate(
            message.getCertificateRelationParentId() == null ? null :
                certificateEntityRepository.findByCertificateId(
                        message.getCertificateRelationParentId())
                    .orElse(null)
        )
        .parentRelationType(
            relationTypeRepository.findOrCreate(message.getCertificateRelationParentType())
        )
        .message(messageEntity)
        .unit(unitRepository.findOrCreate(message.getEventUnitId()))
        .careProvider(careProviderRepository.findOrCreate(message.getEventCareProviderId()))
        .certificateUnit(unitRepository.findOrCreate(message.getCertificateUnitId()))
        .certificateCareProvider(
            careProviderRepository.findOrCreate(message.getCertificateCareProviderId()))
        .patient(patientRepository.findOrCreate(message.getCertificatePatientId()))
        .user(userRepository.findOrCreate(message.getEventUserId()))
        .session(sessionRepository.findOrCreate(message.getEventSessionId()))
        .timestamp(message.getEventTimestamp())
        .origin(originRepository.findOrCreate(message.getEventOrigin()))
        .eventType(eventTypeRepository.findOrCreate(message.getEventMessageType()))
        .role(roleRepository.findOrCreate(message.getEventRole()))
        .sender(
            message.getMessageSenderId() == null ? null :
                partyRepository.findOrCreate(message.getMessageSenderId())
        )
        .recipient(
            message.getMessageSenderId() == null ?
                partyRepository.findOrCreate(message.getRecipientId()) :
                partyRepository.findOrCreate(message.getMessageRecipientId())
        )
        .messageId(message.getId())
        .messageComplementQuestionIdsCount(
            message.getMessageQuestionIds() == null ? null :
                message.getMessageQuestionIds().size()
        )
        .build();
  }

  public PseudonymizedAnalyticsMessage toDomain(EventEntity entity) {
    final var domainBuilder = PseudonymizedAnalyticsMessage.builder()
        .id(entity.getMessageId())
        .eventTimestamp(entity.getTimestamp())
        .eventMessageType(entity.getEventType().getEventType())
        .certificatePatientId(entity.getPatient().getPatientId())
        .eventUserId(entity.getUser() != null ? entity.getUser().getUserId() : null)
        .eventRole(entity.getRole() != null ? entity.getRole().getRole() : null)
        .eventUnitId(entity.getUnit() != null ? entity.getUnit().getHsaId() : null)
        .eventCareProviderId(
            entity.getCareProvider() != null ? entity.getCareProvider().getHsaId() : null)
        .eventOrigin(entity.getOrigin() != null ? entity.getOrigin().getOrigin() : null)
        .eventSessionId(entity.getSession() != null ? entity.getSession().getSessionId() : null)
        .recipientId(
            entity.getMessage() != null || entity.getRecipient() == null ? null :
                entity.getRecipient().getParty()
        )
        .certificateId(entity.getCertificate().getCertificateId())
        .certificateType(entity.getCertificate().getCertificateType())
        .certificateTypeVersion(entity.getCertificate().getCertificateTypeVersion())
        .certificateRelationParentId(
            entity.getParentRelationCertificate() == null ? null :
                entity.getParentRelationCertificate().getCertificateId()
        )
        .certificateRelationParentType(
            entity.getParentRelationType() == null ? null :
                entity.getParentRelationType().getRelationType()
        )
        .certificateUnitId(entity.getCertificateUnit().getHsaId())
        .certificateCareProviderId(entity.getCertificateCareProvider().getHsaId());

    if (entity.getMessage() != null) {
      final var message = entity.getMessage();
      domainBuilder
          .messageId(message.getMessageId())
          .messageAnswerId(message.getMessageAnswerId())
          .messageReminderId(message.getMessageReminderId())
          .messageType(message.getMessageType())
          .messageSent(message.getSent())
          .messageLastDateToAnswer(message.getLastDateToAnswer())
          .messageQuestionIds(getQuestionIds(message))
          .messageSenderId(entity.getSender().getParty())
          .messageRecipientId(entity.getRecipient().getParty());
    }

    return domainBuilder.build();
  }

  private static List<String> getQuestionIds(MessageEntity message) {
    final var questionIds = new ArrayList<String>(7);
    if (message.getComplementFirstQuestionId() != null) {
      questionIds.add(message.getComplementFirstQuestionId());
    }
    if (message.getComplementSecondQuestionId() != null) {
      questionIds.add(message.getComplementSecondQuestionId());
    }
    if (message.getComplementThirdQuestionId() != null) {
      questionIds.add(message.getComplementThirdQuestionId());
    }
    if (message.getComplementFourthQuestionId() != null) {
      questionIds.add(message.getComplementFourthQuestionId());
    }
    if (message.getComplementFifthQuestionId() != null) {
      questionIds.add(message.getComplementFifthQuestionId());
    }
    if (message.getComplementSixthQuestionId() != null) {
      questionIds.add(message.getComplementSixthQuestionId());
    }
    if (message.getComplementSeventhQuestionId() != null) {
      questionIds.add(message.getComplementSeventhQuestionId());
    }
    return questionIds;
  }
}
