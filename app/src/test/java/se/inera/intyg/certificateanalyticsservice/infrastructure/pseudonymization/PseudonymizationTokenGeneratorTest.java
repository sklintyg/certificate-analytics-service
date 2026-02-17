package se.inera.intyg.certificateanalyticsservice.infrastructure.pseudonymization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(SpringExtension.class)
class PseudonymizationTokenGeneratorTest {

  @InjectMocks
  private PseudonymizationTokenGenerator pseudonymizationTokenGenerator;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(pseudonymizationTokenGenerator, "key",
        "thisisasecretkeythatisusedaspepper".getBytes(StandardCharsets.UTF_8)
    );
    ReflectionTestUtils.setField(pseudonymizationTokenGenerator, "context", "analytics-test");
  }

  /**
   * These tests ensure that the pseudonymization algorithm produces the same output for the same
   * input over time, given a fixed key and context. If this test fails, it indicates that the
   * algorithm or its parameters have changed, which could impact data consistency and integrity.
   */
  @Nested
  class TestConsistencyOverTime {

    @Test
    void shallGeneratePseudonymizedIdThatIsTheSameOverTime() {
      final var expected = "bco7K7Z0nYQVgUuhp3HdOQ";
      final var id = "id";

      final var actual = pseudonymizationTokenGenerator.id(id);

      assertEquals(expected, actual);
    }

    @Test
    void shallGeneratePseudonymizedEventUnitIdThatIsTheSameOverTime() {
      final var expected = "r6fVcMsYtdKBEYPlJsd1jA";
      final var eventUnitId = "eventUnitId";

      final var actual = pseudonymizationTokenGenerator.eventUnitId(eventUnitId);

      assertEquals(expected, actual);
    }

    @Test
    void shallGeneratePseudonymizedEventCareProviderIdThatIsTheSameOverTime() {
      final var expected = "Fcap4iWFtF3qQ5F_-PsL-g";
      final var eventCareProviderId = "eventCareProviderId";

      final var actual = pseudonymizationTokenGenerator.eventCareProviderId(eventCareProviderId);

      assertEquals(expected, actual);
    }

    @Test
    void shallGeneratePseudonymizedStaffIdThatIsTheSameOverTime() {
      final var expected = "q4mqOSKxPQwxpUoB6eqFJg";
      final var staffId = "staffId";

      final var actual = pseudonymizationTokenGenerator.staffId(staffId);

      assertEquals(expected, actual);
    }

    @Test
    void shallGeneratePseudonymizedCertificateIdThatIsTheSameOverTime() {
      final var expected = "y3iWsfAvFHqP4xtd7qcBLA";
      final var certificateId = "certificateId";

      final var actual = pseudonymizationTokenGenerator.certificateId(certificateId);

      assertEquals(expected, actual);
    }

    @Test
    void shallGeneratePseudonymizedParentCertificateIdThatIsTheSameOverTime() {
      final var expected = "3CMu4xpRBVNUVQ9ARZKY8Q";
      final var parentCertificateId = "parentCertificateId";

      final var actual = pseudonymizationTokenGenerator.parentCertificateId(parentCertificateId);

      assertEquals(expected, actual);
    }

    @Test
    void shallGeneratePseudonymizedCertificateUnitIdThatIsTheSameOverTime() {
      final var expected = "h5WSwFQuQwDwcnsLIqekBA";
      final var certificateUnitId = "certificateUnitId";

      final var actual = pseudonymizationTokenGenerator.certificateUnitId(certificateUnitId);

      assertEquals(expected, actual);
    }

    @Test
    void shallGeneratePseudonymizedCertificateCareProviderIdThatIsTheSameOverTime() {
      final var expected = "ztzNiCshMjAcTrGZzrQXrA";
      final var certificateCareProviderId = "certificateCareProviderId";

      final var actual = pseudonymizationTokenGenerator.certificateCareProviderId(
          certificateCareProviderId);

      assertEquals(expected, actual);
    }

    @Test
    void shallGeneratePseudonymizedSessionIdThatIsTheSameOverTime() {
      final var expected = "nTxVe__MOq5J6sbFWhRweg";
      final var sessionId = "sessionId";

      final var actual = pseudonymizationTokenGenerator.sessionId(sessionId);

      assertEquals(expected, actual);
    }

    @Test
    void shallGeneratePseudonymizedPatientIdThatIsTheSameOverTime() {
      final var expected = "3M8x-GCLyXq0hXzdpmquZA";
      final var patientId = "patientId";

      final var actual = pseudonymizationTokenGenerator.patientId(patientId);

      assertEquals(expected, actual);
    }

    @Test
    void shallGeneratePseudonymizedMessageIdThatIsTheSameOverTime() {
      final var expected = "kWK4wB8ZyUZThriVDvMOTQ";
      final var messageId = "messageId";

      final var actual = pseudonymizationTokenGenerator.messageId(messageId);

      assertEquals(expected, actual);
    }

    @Test
    void shallGeneratePseudonymizedMessageAnswerIdThatIsTheSameOverTime() {
      final var expected = "LsGj-V3YAwae8YDFFtgUwQ";
      final var messageAnswerId = "messageAnswerId";

      final var actual = pseudonymizationTokenGenerator.messageAnswerId(messageAnswerId);

      assertEquals(expected, actual);
    }

    @Test
    void shallGeneratePseudonymizedMessageReminderIdThatIsTheSameOverTime() {
      final var expected = "TxmPHpti7oq2IUE2dcFuAw";
      final var messageReminderId = "messageReminderId";

      final var actual = pseudonymizationTokenGenerator.messageReminderId(messageReminderId);

      assertEquals(expected, actual);
    }
  }

  /**
   * These tests ensure that the pseudonymization algorithm produces the same output for the same
   * input values when called multiple times. This is crucial for maintaining consistent
   * pseudonymized identifiers across different parts of the system that may process the same data.
   */
  @Nested
  class TestConsistencyBetweenCalls {

    @Test
    void shallGenerateSamePseudonymizedIdFromSameValue() {
      final var id = "id";

      final var idOne = pseudonymizationTokenGenerator.id(id);
      final var idTwo = pseudonymizationTokenGenerator.id(id);

      assertEquals(idOne, idTwo);
    }

    @Test
    void shallGenerateSamePseudonymizedStaffIdFromSameValue() {
      final var staffId = "staffId";

      final var staffIdOne = pseudonymizationTokenGenerator.staffId(staffId);
      final var staffIdTwo = pseudonymizationTokenGenerator.staffId(staffId);

      assertEquals(staffIdOne, staffIdTwo);
    }

    @Test
    void shallGenerateSamePseudonymizedEventUnitIdFromSameValue() {
      final var eventUnitId = "eventUnitId";

      final var eventUnitIdOne = pseudonymizationTokenGenerator.eventUnitId(eventUnitId);
      final var eventUnitIdTwo = pseudonymizationTokenGenerator.eventUnitId(eventUnitId);

      assertEquals(eventUnitIdOne, eventUnitIdTwo);
    }

    @Test
    void shallGenerateSamePseudonymizedEventCareProviderIdFromSameValue() {
      final var eventCareProviderId = "eventCareProviderId";

      final var eventCareProviderIdOne = pseudonymizationTokenGenerator.eventCareProviderId(
          eventCareProviderId);
      final var eventCareProviderIdTwo = pseudonymizationTokenGenerator.eventCareProviderId(
          eventCareProviderId);

      assertEquals(eventCareProviderIdOne, eventCareProviderIdTwo);
    }

    @Test
    void shallGenerateSamePseudonymizedCertificateIdFromSameValue() {
      final var certificateId = "certificateId";

      final var certificateIdOne = pseudonymizationTokenGenerator.certificateId(certificateId);
      final var certificateIdTwo = pseudonymizationTokenGenerator.certificateId(certificateId);

      assertEquals(certificateIdOne, certificateIdTwo);
    }

    @Test
    void shallGenerateSamePseudonymizedParentCertificateIdFromSameValue() {
      final var parentCertificateId = "parentCertificateId";

      final var parentCertificateIdOne = pseudonymizationTokenGenerator.parentCertificateId(
          parentCertificateId);
      final var parentCertificateIdTwo = pseudonymizationTokenGenerator.parentCertificateId(
          parentCertificateId);

      assertEquals(parentCertificateIdOne, parentCertificateIdTwo);
    }

    @Test
    void shallGenerateSamePseudonymizedCertificateUnitIdFromSameValue() {
      final var certificateUnitId = "certificateUnitId";

      final var certificateUnitIdOne = pseudonymizationTokenGenerator.certificateUnitId(
          certificateUnitId);
      final var certificateUnitIdTwo = pseudonymizationTokenGenerator.certificateUnitId(
          certificateUnitId);

      assertEquals(certificateUnitIdOne, certificateUnitIdTwo);
    }

    @Test
    void shallGenerateSamePseudonymizedCertificateCareProviderIdFromSameValue() {
      final var certificateCareProviderId = "certificateCareProviderId";

      final var certificateCareProviderIdOne = pseudonymizationTokenGenerator.certificateCareProviderId(
          certificateCareProviderId);
      final var certificateCareProviderIdTwo = pseudonymizationTokenGenerator.certificateCareProviderId(
          certificateCareProviderId);

      assertEquals(certificateCareProviderIdOne, certificateCareProviderIdTwo);
    }

    @Test
    void shallGenerateSamePseudonymizedSessionIdFromSameValue() {
      final var sessionId = "sessionId";

      final var sessionIdOne = pseudonymizationTokenGenerator.sessionId(sessionId);
      final var sessionIdTwo = pseudonymizationTokenGenerator.sessionId(sessionId);

      assertEquals(sessionIdOne, sessionIdTwo);
    }

    @Test
    void shallGenerateSamePseudonymizedPatientIdFromSameValue() {
      final var patientId = "patientId";

      final var patientIdOne = pseudonymizationTokenGenerator.patientId(patientId);
      final var patientIdTwo = pseudonymizationTokenGenerator.patientId(patientId);

      assertEquals(patientIdOne, patientIdTwo);
    }

    @Test
    void shallGenerateSamePseudonymizedMessageIdFromSameValue() {
      final var messageId = "messageId";

      final var messageIdOne = pseudonymizationTokenGenerator.messageId(messageId);
      final var messageIdTwo = pseudonymizationTokenGenerator.messageId(messageId);

      assertEquals(messageIdOne, messageIdTwo);
    }

    @Test
    void shallGenerateSamePseudonymizedMessageAnswerIdFromSameValue() {
      final var messageAnswerId = "messageAnswerId";

      final var messageAnswerIdOne = pseudonymizationTokenGenerator.messageAnswerId(
          messageAnswerId);
      final var messageAnswerIdTwo = pseudonymizationTokenGenerator.messageAnswerId(
          messageAnswerId);

      assertEquals(messageAnswerIdOne, messageAnswerIdTwo);
    }

    @Test
    void shallGenerateSamePseudonymizedMessageReminderIdFromSameValue() {
      final var messageReminderId = "messageReminderId";

      final var messageReminderIdOne = pseudonymizationTokenGenerator.messageReminderId(
          messageReminderId);
      final var messageReminderIdTwo = pseudonymizationTokenGenerator.messageReminderId(
          messageReminderId);

      assertEquals(messageReminderIdOne, messageReminderIdTwo);
    }
  }

  /**
   * These tests ensure that the pseudonymization algorithm generates unique outputs for the same
   * input value when the input is associated with different types of identifiers. This is important
   * to prevent collisions and ensure that identifiers remain distinct even if they share the same
   * original value.
   */
  @Nested
  class TestTokenTypeUniqueness {

    @Test
    void shallGenerateDifferentTokensForSameValueAcrossTypes() {
      final var value = "samevalue";

      final var idToken = pseudonymizationTokenGenerator.id(value);
      final var staffIdToken = pseudonymizationTokenGenerator.staffId(value);
      final var sessionIdToken = pseudonymizationTokenGenerator.sessionId(value);
      final var certificateIdToken = pseudonymizationTokenGenerator.certificateId(value);
      final var unitIdToken = pseudonymizationTokenGenerator.eventUnitId(value);
      final var careProviderIdToken = pseudonymizationTokenGenerator.eventCareProviderId(value);
      final var patientIdToken = pseudonymizationTokenGenerator.patientId(value);
      final var messageIdToken = pseudonymizationTokenGenerator.messageId(value);

      final var tokens = List.of(
          idToken,
          staffIdToken,
          sessionIdToken,
          certificateIdToken,
          unitIdToken,
          careProviderIdToken,
          patientIdToken,
          messageIdToken
      );

      final var uniqueTokens = tokens.stream().distinct().count();

      assertEquals(tokens.size(), uniqueTokens,
          "Tokens for same value across types should be unique: %s".formatted(tokens)
      );
    }

    @Test
    void shallGenerateSameTokenForSameValueForCertificateIdTypes() {
      final var value = "samevalue";

      final var certificateIdToken = pseudonymizationTokenGenerator.certificateId(value);
      final var parentCertificateIdToken = pseudonymizationTokenGenerator.parentCertificateId(
          value);

      final var tokens = List.of(
          certificateIdToken,
          parentCertificateIdToken
      );

      final var uniqueTokens = tokens.stream().distinct().count();

      assertEquals(1, uniqueTokens,
          "Tokens for same value across certificateId types should be same: %s".formatted(tokens)
      );
    }

    @Test
    void shallGenerateSameTokenForSameValueForUnitIdTypes() {
      final var value = "samevalue";

      final var eventUnitIdToken = pseudonymizationTokenGenerator.eventUnitId(value);
      final var certificateUnitIdToken = pseudonymizationTokenGenerator.certificateUnitId(
          value);

      final var tokens = List.of(
          eventUnitIdToken,
          certificateUnitIdToken
      );

      final var uniqueTokens = tokens.stream().distinct().count();

      assertEquals(1, uniqueTokens,
          "Tokens for same value across unitId types should be same: %s".formatted(tokens)
      );
    }

    @Test
    void shallGenerateSameTokenForSameValueForCareProviderIdTypes() {
      final var value = "samevalue";

      final var eventCareProviderIdToken = pseudonymizationTokenGenerator.eventCareProviderId(
          value);
      final var certificateCareProviderIdToken = pseudonymizationTokenGenerator.certificateCareProviderId(
          value);

      final var tokens = List.of(
          eventCareProviderIdToken,
          certificateCareProviderIdToken
      );

      final var uniqueTokens = tokens.stream().distinct().count();

      assertEquals(1, uniqueTokens,
          "Tokens for same value across careProviderId types should be same: %s".formatted(tokens)
      );
    }

    @Test
    void shallGenerateSameTokenForSameValueForMessageIdTypes() {
      final var value = "samevalue";

      final var messageIdToken = pseudonymizationTokenGenerator.messageId(value);
      final var messageAnswerIdToken = pseudonymizationTokenGenerator.messageAnswerId(value);
      final var messageReminderIdToken = pseudonymizationTokenGenerator.messageReminderId(value);

      final var tokens = List.of(
          messageIdToken,
          messageAnswerIdToken,
          messageReminderIdToken
      );

      final var uniqueTokens = tokens.stream().distinct().count();

      assertEquals(1, uniqueTokens,
          "Tokens for same value across messageId types should be same: %s".formatted(tokens)
      );
    }
  }

  /**
   * These tests ensure that the pseudonymization algorithm produces different outputs for the same
   * input when the context is changed. This is important for scenarios where the same identifier
   * might be used in different environments or applications, and we want to ensure that the
   * pseudonymized values remain distinct across these contexts.
   */
  @Nested
  class TestUniquenessBetweenDifferentContexts {

    @Test
    void shallGenerateDifferentPseudonymizedIdFromSameValueButDifferentContext() {
      final var id = "id";

      final var idOne = pseudonymizationTokenGenerator.id(id);
      ReflectionTestUtils.setField(pseudonymizationTokenGenerator, "context", "analytics-dev");
      final var idTwo = pseudonymizationTokenGenerator.id(id);

      assertNotEquals(idOne, idTwo);
    }

    @Test
    void shallGenerateDifferentPseudonymizedEventUnitIdFromSameValueButDifferentContext() {
      final var eventUnitId = "eventUnitId";

      final var eventUnitIdOne = pseudonymizationTokenGenerator.eventUnitId(eventUnitId);
      ReflectionTestUtils.setField(pseudonymizationTokenGenerator, "context", "analytics-dev");
      final var eventUnitIdTwo = pseudonymizationTokenGenerator.eventUnitId(eventUnitId);

      assertNotEquals(eventUnitIdOne, eventUnitIdTwo);
    }

    @Test
    void shallGenerateDifferentPseudonymizedEventCareProviderIdFromSameValueButDifferentContext() {
      final var eventCareProviderId = "eventCareProviderId";

      final var eventCareProviderIdOne = pseudonymizationTokenGenerator.eventCareProviderId(
          eventCareProviderId);
      ReflectionTestUtils.setField(pseudonymizationTokenGenerator, "context", "analytics-dev");
      final var eventCareProviderIdTwo = pseudonymizationTokenGenerator.eventCareProviderId(
          eventCareProviderId);

      assertNotEquals(eventCareProviderIdOne, eventCareProviderIdTwo);
    }

    @Test
    void shallGenerateDifferentPseudonymizedStaffIdFromSameValueButDifferentContext() {
      final var staffId = "staffId";

      final var staffIdOne = pseudonymizationTokenGenerator.staffId(staffId);
      ReflectionTestUtils.setField(pseudonymizationTokenGenerator, "context", "analytics-dev");
      final var staffIdTwo = pseudonymizationTokenGenerator.staffId(staffId);

      assertNotEquals(staffIdOne, staffIdTwo);
    }

    @Test
    void shallGenerateDifferentPseudonymizedCertificateIdFromSameValueButDifferentContext() {
      final var certificateId = "certificateId";

      final var certificateIdOne = pseudonymizationTokenGenerator.certificateId(certificateId);
      ReflectionTestUtils.setField(pseudonymizationTokenGenerator, "context", "analytics-dev");
      final var certificateIdTwo = pseudonymizationTokenGenerator.certificateId(certificateId);

      assertNotEquals(certificateIdOne, certificateIdTwo);
    }

    @Test
    void shallGenerateDifferentPseudonymizedParentCertificateIdFromSameValueButDifferentContext() {
      final var parentCertificateId = "parentCertificateId";

      final var parentCertificateIdOne = pseudonymizationTokenGenerator.parentCertificateId(
          parentCertificateId);
      ReflectionTestUtils.setField(pseudonymizationTokenGenerator, "context", "analytics-dev");
      final var parentCertificateIdTwo = pseudonymizationTokenGenerator.parentCertificateId(
          parentCertificateId);

      assertNotEquals(parentCertificateIdOne, parentCertificateIdTwo);
    }

    @Test
    void shallGenerateDifferentPseudonymizedCertificateUnitIdFromSameValueButDifferentContext() {
      final var certificateUnitId = "certificateUnitId";

      final var certificateUnitIdOne = pseudonymizationTokenGenerator.certificateUnitId(
          certificateUnitId);
      ReflectionTestUtils.setField(pseudonymizationTokenGenerator, "context", "analytics-dev");
      final var certificateUnitIdTwo = pseudonymizationTokenGenerator.certificateUnitId(
          certificateUnitId);

      assertNotEquals(certificateUnitIdOne, certificateUnitIdTwo);
    }

    @Test
    void shallGenerateDifferentPseudonymizedCertificateCareProviderIdFromSameValueButDifferentContext() {
      final var certificateCareProviderId = "certificateCareProviderId";

      final var certificateCareProviderIdOne = pseudonymizationTokenGenerator.certificateCareProviderId(
          certificateCareProviderId);
      ReflectionTestUtils.setField(pseudonymizationTokenGenerator, "context", "analytics-dev");
      final var certificateCareProviderIdTwo = pseudonymizationTokenGenerator.certificateCareProviderId(
          certificateCareProviderId);

      assertNotEquals(certificateCareProviderIdOne, certificateCareProviderIdTwo);
    }

    @Test
    void shallGenerateDifferentPseudonymizedSessionIdFromSameValueButDifferentContext() {
      final var sessionId = "sessionId";

      final var sessionIdOne = pseudonymizationTokenGenerator.sessionId(sessionId);
      ReflectionTestUtils.setField(pseudonymizationTokenGenerator, "context", "analytics-dev");
      final var sessionIdTwo = pseudonymizationTokenGenerator.sessionId(sessionId);

      assertNotEquals(sessionIdOne, sessionIdTwo);
    }

    @Test
    void shallGenerateDifferentPseudonymizedPatientIdFromSameValueButDifferentContext() {
      final var patientId = "patientId";

      final var patientIdOne = pseudonymizationTokenGenerator.patientId(patientId);
      ReflectionTestUtils.setField(pseudonymizationTokenGenerator, "context", "analytics-dev");
      final var patientIdTwo = pseudonymizationTokenGenerator.patientId(patientId);

      assertNotEquals(patientIdOne, patientIdTwo);
    }

    @Test
    void shallGenerateDifferentPseudonymizedMessageIdFromSameValueButDifferentContext() {
      final var messageId = "messageId";

      final var messageIdOne = pseudonymizationTokenGenerator.messageId(messageId);
      ReflectionTestUtils.setField(pseudonymizationTokenGenerator, "context", "analytics-dev");
      final var messageIdTwo = pseudonymizationTokenGenerator.messageId(messageId);

      assertNotEquals(messageIdOne, messageIdTwo);
    }

    @Test
    void shallGenerateDifferentPseudonymizedMessageAnswerIdFromSameValueButDifferentContext() {
      final var messageAnswerId = "messageAnswerId";

      final var messageAnswerIdOne = pseudonymizationTokenGenerator.messageAnswerId(
          messageAnswerId);
      ReflectionTestUtils.setField(pseudonymizationTokenGenerator, "context", "analytics-dev");
      final var messageAnswerIdTwo = pseudonymizationTokenGenerator.messageAnswerId(
          messageAnswerId);

      assertNotEquals(messageAnswerIdOne, messageAnswerIdTwo);
    }

    @Test
    void shallGenerateDifferentPseudonymizedMessageReminderIdFromSameValueButDifferentContext() {
      final var messageReminderId = "messageReminderId";

      final var messageReminderIdOne = pseudonymizationTokenGenerator.messageReminderId(
          messageReminderId);
      ReflectionTestUtils.setField(pseudonymizationTokenGenerator, "context", "analytics-dev");
      final var messageReminderIdTwo = pseudonymizationTokenGenerator.messageReminderId(
          messageReminderId);

      assertNotEquals(messageReminderIdOne, messageReminderIdTwo);
    }
  }
}