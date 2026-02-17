package se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataEntities.careProviderEntity;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataEntities.messageEntity;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataEntities.messageSentEventEntityBuilder;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataEntities.patientEntity;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataEntities.relationTypeEntityBuilder;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataEntities.sentEventEntityBuilder;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataEntities.unitEntity;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataPseudonymized.messagePseudonymizedMessageBuilder;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataPseudonymized.sentPseudonymizedMessageBuilder;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.CertificateEntity;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.EventEntity;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.EventTypeEntity;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.OriginEntity;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.PartyEntity;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.RoleEntity;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.SessionEntity;
import se.inera.intyg.certificateanalyticsservice.infrastructure.persistance.entity.UserEntity;
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

@ExtendWith(MockitoExtension.class)
class EventMapperTest {

  @InjectMocks
  private EventMapper eventMapper;
  @Mock
  private UnitRepository unitRepository;
  @Mock
  private CareProviderRepository careProviderRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private SessionRepository sessionRepository;
  @Mock
  private OriginRepository originRepository;
  @Mock
  private EventTypeRepository eventTypeRepository;
  @Mock
  private RoleRepository roleRepository;
  @Mock
  private PartyRepository partyRepository;
  @Mock
  private CertificateEntityMapper certificateEntityMapper;
  @Mock
  private MessageEntityMapper messageEntityMapper;
  @Mock
  private PatientRepository patientRepository;
  @Mock
  private CertificateEntityRepository certificateEntityRepository;
  @Mock
  private RelationTypeRepository relationTypeRepository;

  @Test
  void shouldMapPseudonymizedAnalyticsMessageCorrectly() {
    final var message = sentPseudonymizedMessageBuilder().build();
    final var expectedCertificate = mock(CertificateEntity.class);
    final var expectedParentCertificate = mock(CertificateEntity.class);
    final var expectedUnit = unitEntity();
    final var expectedCareProvider = careProviderEntity();
    final var expectedUser = mock(UserEntity.class);
    final var expectedSession = mock(SessionEntity.class);
    final var expectedOrigin = mock(OriginEntity.class);
    final var expectedEventType = mock(EventTypeEntity.class);
    final var expectedRole = mock(RoleEntity.class);
    final var expectedRecipient = mock(PartyEntity.class);
    final var expectedPatient = patientEntity();
    final var expectedCertificateUnit = unitEntity();
    final var expectedCertificateCareProvider = careProviderEntity();
    final var expectedRelationType = relationTypeEntityBuilder().build();

    when(certificateEntityMapper.map(message)).thenReturn(expectedCertificate);
    when(certificateEntityRepository.findByCertificateId(message.getCertificateRelationParentId()))
        .thenReturn(Optional.of(expectedParentCertificate));
    when(relationTypeRepository.findOrCreate(message.getCertificateRelationParentType()))
        .thenReturn(expectedRelationType);
    when(unitRepository.findOrCreate(message.getEventUnitId())).thenReturn(expectedUnit);
    when(careProviderRepository.findOrCreate(message.getEventCareProviderId())).thenReturn(
        expectedCareProvider);
    when(userRepository.findOrCreate(message.getEventUserId())).thenReturn(expectedUser);
    when(sessionRepository.findOrCreate(message.getEventSessionId())).thenReturn(expectedSession);
    when(originRepository.findOrCreate(message.getEventOrigin())).thenReturn(expectedOrigin);
    when(eventTypeRepository.findOrCreate(message.getEventMessageType())).thenReturn(
        expectedEventType);
    when(roleRepository.findOrCreate(message.getEventRole())).thenReturn(expectedRole);
    when(partyRepository.findOrCreate(message.getRecipientId())).thenReturn(expectedRecipient);
    when(patientRepository.findOrCreate(message.getCertificatePatientId()))
        .thenReturn(expectedPatient);

    final var expected = EventEntity.builder()
        .certificate(expectedCertificate)
        .parentRelationCertificate(expectedParentCertificate)
        .parentRelationType(expectedRelationType)
        .certificateUnit(expectedCertificateUnit)
        .certificateCareProvider(expectedCertificateCareProvider)
        .patient(expectedPatient)
        .unit(expectedUnit)
        .careProvider(expectedCareProvider)
        .user(expectedUser)
        .session(expectedSession)
        .timestamp(message.getEventTimestamp())
        .origin(expectedOrigin)
        .eventType(expectedEventType)
        .role(expectedRole)
        .recipient(expectedRecipient)
        .messageId(message.getId())
        .build();

    final var result = eventMapper.toEntity(message);
    assertEquals(expected, result);
  }

  @Test
  void shouldMapPseudonymizedAnalyticsMessageWithMessageCorrectly() {
    final var message = messagePseudonymizedMessageBuilder().build();
    final var expectedCertificate = mock(CertificateEntity.class);
    final var expectedUnit = unitEntity();
    final var expectedCareProvider = careProviderEntity();
    final var expectedUser = mock(UserEntity.class);
    final var expectedSession = mock(SessionEntity.class);
    final var expectedOrigin = mock(OriginEntity.class);
    final var expectedEventType = mock(EventTypeEntity.class);
    final var expectedRole = mock(RoleEntity.class);
    final var expectedSender = mock(PartyEntity.class);
    final var expectedRecipient = mock(PartyEntity.class);
    final var expectedCertificateUnit = unitEntity();
    final var expectedCertificateCareProvider = careProviderEntity();
    final var expectedMessage = messageEntity().build();

    when(certificateEntityMapper.map(message)).thenReturn(expectedCertificate);
    when(messageEntityMapper.map(message)).thenReturn(expectedMessage);
    when(unitRepository.findOrCreate(message.getEventUnitId())).thenReturn(expectedUnit);
    when(careProviderRepository.findOrCreate(message.getEventCareProviderId())).thenReturn(
        expectedCareProvider);
    when(userRepository.findOrCreate(message.getEventUserId())).thenReturn(expectedUser);
    when(sessionRepository.findOrCreate(message.getEventSessionId())).thenReturn(expectedSession);
    when(originRepository.findOrCreate(message.getEventOrigin())).thenReturn(expectedOrigin);
    when(eventTypeRepository.findOrCreate(message.getEventMessageType())).thenReturn(
        expectedEventType);
    when(roleRepository.findOrCreate(message.getEventRole())).thenReturn(expectedRole);
    when(partyRepository.findOrCreate(message.getMessageSenderId())).thenReturn(expectedSender);
    when(partyRepository.findOrCreate(message.getMessageRecipientId())).thenReturn(
        expectedRecipient);

    final var expected = EventEntity.builder()
        .certificate(expectedCertificate)
        .certificateUnit(expectedCertificateUnit)
        .certificateCareProvider(expectedCertificateCareProvider)
        .unit(expectedUnit)
        .careProvider(expectedCareProvider)
        .user(expectedUser)
        .session(expectedSession)
        .timestamp(message.getEventTimestamp())
        .origin(expectedOrigin)
        .eventType(expectedEventType)
        .role(expectedRole)
        .sender(expectedSender)
        .recipient(expectedRecipient)
        .messageId(message.getId())
        .message(expectedMessage)
        .messageComplementQuestionIdsCount(message.getMessageQuestionIds().size())
        .build();

    final var result = eventMapper.toEntity(message);
    assertEquals(expected, result);
  }

  @Test
  void shouldMapEventEntityToDomainCorrectly() {
    final var expected = sentPseudonymizedMessageBuilder().build();
    final var entity = sentEventEntityBuilder().build();

    final var actual = eventMapper.toDomain(entity);

    assertEquals(expected, actual);
  }

  @Test
  void shouldMapEventEntityToDomainWhenMessageIsPresent() {
    final var expected = messagePseudonymizedMessageBuilder().build();
    final var entity = messageSentEventEntityBuilder().build();

    final var actual = eventMapper.toDomain(entity);

    assertEquals(expected, actual);
  }
}
