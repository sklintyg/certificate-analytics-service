package se.inera.intyg.certificateanalyticsservice.testdata;

import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.CARE_PROVIDER_ID;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.CERTIFICATE_PARENT_TYPE;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.CERTIFICATE_TYPE;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.CERTIFICATE_TYPE_VERSION;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.EVENT_TYPE_CERTIFICATE_SENT;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.EVENT_TYPE_COMPLEMENT_FROM_RECIPIENT;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.HASHED_CERTIFICATE_ID;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.HASHED_CERTIFICATE_PARENT_ID;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.HASHED_ID;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.HASHED_MESSAGE_ANSWER_ID;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.HASHED_MESSAGE_ID;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.HASHED_MESSAGE_REMINDER_ID;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.HASHED_PATIENT_ID;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.HASHED_SESSION_ID;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.HASHED_USER_ID;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.MESSAGE_LAST_DATE_TO_ANSWER;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.MESSAGE_QUESTION_ID_1;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.MESSAGE_QUESTION_ID_2;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.MESSAGE_RECIPIENT;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.MESSAGE_SENDER;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.MESSAGE_SENT;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.MESSAGE_TYPE;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.ORIGIN;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.RECIPIENT;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.ROLE;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.TIMESTAMP;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.UNIT_ID;

import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.CareProviderEntity;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.CertificateEntity;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.CertificateEntity.CertificateEntityBuilder;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.EventEntity;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.EventEntity.EventEntityBuilder;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.EventTypeEntity;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.MessageEntity;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.OriginEntity;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.PartyEntity;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.PatientEntity;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.RelationTypeEntity;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.RelationTypeEntity.RelationTypeEntityBuilder;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.RoleEntity;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.SessionEntity;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.UnitEntity;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.UserEntity;

public class TestDataEntities {

  private TestDataEntities() {
    throw new IllegalStateException("Utility class");
  }

  public static EventEntityBuilder sentEventEntityBuilder() {
    return EventEntity.builder()
        .certificate(certificateEntity().build())
        .parentRelationCertificate(
            certificateEntity()
                .certificateId(HASHED_CERTIFICATE_PARENT_ID)
                .build()
        )
        .parentRelationType(relationTypeEntityBuilder().build())
        .certificateUnit(unitEntity())
        .certificateCareProvider(careProviderEntity())
        .messageId(HASHED_ID)
        .timestamp(TIMESTAMP)
        .eventType(eventTypeEntity())
        .role(roleEntity())
        .patient(patientEntity())
        .unit(unitEntity())
        .careProvider(careProviderEntity())
        .user(userEntity())
        .session(sessionEntity())
        .origin(originEntity())
        .recipient(recipientPartyEntity());
  }

  public static EventEntityBuilder messageSentEventEntityBuilder() {
    return EventEntity.builder()
        .certificate(certificateEntity().build())
        .certificateUnit(unitEntity())
        .certificateCareProvider(careProviderEntity())
        .message(messageEntity().build())
        .messageId(HASHED_ID)
        .timestamp(TIMESTAMP)
        .eventType(
            EventTypeEntity.builder()
                .eventType(EVENT_TYPE_COMPLEMENT_FROM_RECIPIENT)
                .build()
        )
        .role(roleEntity())
        .patient(patientEntity())
        .unit(unitEntity())
        .careProvider(careProviderEntity())
        .user(userEntity())
        .session(sessionEntity())
        .origin(originEntity())
        .sender(messageSenderPartyEntity())
        .recipient(messageRecipientPartyEntity());
  }

  public static CertificateEntityBuilder certificateEntity() {
    return CertificateEntity.builder()
        .certificateId(HASHED_CERTIFICATE_ID)
        .certificateType(CERTIFICATE_TYPE)
        .certificateTypeVersion(CERTIFICATE_TYPE_VERSION);
  }

  public static PatientEntity patientEntity() {
    return PatientEntity.builder()
        .patientId(HASHED_PATIENT_ID)
        .build();
  }

  public static CareProviderEntity careProviderEntity() {
    return CareProviderEntity.builder()
        .hsaId(CARE_PROVIDER_ID)
        .build();
  }

  public static UnitEntity unitEntity() {
    return UnitEntity.builder()
        .hsaId(UNIT_ID)
        .build();
  }

  public static UserEntity userEntity() {
    return UserEntity.builder()
        .userId(HASHED_USER_ID)
        .build();
  }

  public static SessionEntity sessionEntity() {
    return SessionEntity.builder()
        .sessionId(HASHED_SESSION_ID)
        .build();
  }

  public static OriginEntity originEntity() {
    return OriginEntity.builder()
        .origin(ORIGIN)
        .build();
  }

  public static EventTypeEntity eventTypeEntity() {
    return EventTypeEntity.builder()
        .eventType(EVENT_TYPE_CERTIFICATE_SENT)
        .build();
  }

  public static RelationTypeEntityBuilder relationTypeEntityBuilder() {
    return RelationTypeEntity.builder()
        .relationType(CERTIFICATE_PARENT_TYPE);
  }

  public static RoleEntity roleEntity() {
    return RoleEntity.builder()
        .role(ROLE)
        .build();
  }

  public static PartyEntity recipientPartyEntity() {
    return PartyEntity.builder()
        .party(RECIPIENT)
        .build();
  }

  public static PartyEntity messageSenderPartyEntity() {
    return PartyEntity.builder()
        .party(MESSAGE_SENDER)
        .build();
  }

  public static PartyEntity messageRecipientPartyEntity() {
    return PartyEntity.builder()
        .party(MESSAGE_RECIPIENT)
        .build();
  }

  public static MessageEntity.MessageEntityBuilder messageEntity() {
    return MessageEntity.builder()
        .messageId(HASHED_MESSAGE_ID)
        .messageAnswerId(HASHED_MESSAGE_ANSWER_ID)
        .messageReminderId(HASHED_MESSAGE_REMINDER_ID)
        .messageType(MESSAGE_TYPE)
        .sent(MESSAGE_SENT)
        .lastDateToAnswer(MESSAGE_LAST_DATE_TO_ANSWER)
        .complementFirstQuestionId(MESSAGE_QUESTION_ID_1)
        .complementSecondQuestionId(MESSAGE_QUESTION_ID_2);
  }
}
