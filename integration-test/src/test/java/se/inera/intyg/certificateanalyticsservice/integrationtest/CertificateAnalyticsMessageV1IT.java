package se.inera.intyg.certificateanalyticsservice.integrationtest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static se.inera.intyg.certificateanalyticsservice.testability.configuration.TestabilityConfiguration.TESTABILITY_PROFILE;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataConstants.CERTIFICATE_PARENT_ID;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataMessages.draftMessageBuilder;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataMessages.draftPrivatePractitionerMessageBuilder;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataMessages.receivedQuestionMessageBuilder;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataMessages.replacedRelationBuilder;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataMessages.sentCertificateBuilder;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataMessages.sentMessageBuilder;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataPseudonymized.draftPrivatePractitionerPseudonymizedMessageBuilder;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataPseudonymized.draftPseudonymizedMessageBuilder;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataPseudonymized.messagePseudonymizedMessageBuilder;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataPseudonymized.sentPseudonymizedMessageBuilder;

import java.time.Duration;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ActiveProfiles;
import se.inera.intyg.certificateanalyticsservice.integrationtest.util.Containers;
import se.inera.intyg.certificateanalyticsservice.integrationtest.util.JmsUtil;
import se.inera.intyg.certificateanalyticsservice.integrationtest.util.TestabilityUtil;

@ActiveProfiles({"integration-test", TESTABILITY_PROFILE})
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CertificateAnalyticsMessageV1IT {

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private JmsTemplate jmsTemplate;

  @Value("${certificate.analytics.message.queue.name}")
  private String queueName;

  @LocalServerPort
  private int port;

  private JmsUtil jmsUtil;
  private TestabilityUtil testabilityUtil;

  @BeforeAll
  static void beforeAll() {
    Containers.ensureRunning();
  }

  @BeforeEach
  void setUp() {
    jmsUtil = new JmsUtil(jmsTemplate, queueName);
    testabilityUtil = new TestabilityUtil(restTemplate, port);

    jmsUtil.reset();
    testabilityUtil.reset();
  }

  @Test
  void shallProcessAndPersistMessage() {
    final var expected = draftPseudonymizedMessageBuilder().build();
    final var message = draftMessageBuilder().build();

    jmsUtil.publishMessage(message);

    final var actual = testabilityUtil.awaitProcessed(
        message.getMessageId(),
        Duration.ofSeconds(5)
    );

    assertEquals(expected, actual);
  }

  @Test
  void shallProcessAndPersistMessageWithRelation() {
    final var expected = sentPseudonymizedMessageBuilder().build();

    final var firstMessage = sentMessageBuilder()
        .messageId(UUID.randomUUID().toString())
        .certificate(
            sentCertificateBuilder()
                .id(CERTIFICATE_PARENT_ID)
                .parent(null)
                .build()
        )
        .build();
    jmsUtil.publishMessage(firstMessage);

    final var message = sentMessageBuilder()
        .certificate(
            sentCertificateBuilder()
                .parent(
                    replacedRelationBuilder()
                        .id(CERTIFICATE_PARENT_ID)
                        .build()
                )
                .build()
        )
        .build();

    jmsUtil.publishMessage(message);

    final var actual = testabilityUtil.awaitProcessed(
        message.getMessageId(),
        Duration.ofSeconds(5)
    );

    assertEquals(expected, actual);
  }

  @Test
  void shallProcessAndPersistMessageWithMessage() {
    final var expected = messagePseudonymizedMessageBuilder().build();
    final var message = receivedQuestionMessageBuilder().build();

    jmsUtil.publishMessage(message);

    final var actual = testabilityUtil.awaitProcessed(
        message.getMessageId(),
        Duration.ofSeconds(5)
    );

    assertEquals(expected, actual);
  }

  @Test
  void shallProcessAndPersistMessageWithPseudonymizedPrivatePractitioner() {
    final var expected = draftPrivatePractitionerPseudonymizedMessageBuilder().build();
    final var message = draftPrivatePractitionerMessageBuilder().build();

    jmsUtil.publishMessage(message);

    final var actual = testabilityUtil.awaitProcessed(
        message.getMessageId(),
        Duration.ofSeconds(5)
    );

    assertEquals(expected, actual);
  }

  @Test
  void shallProcessAndPersistMessageThroughRedelivery() {
    final var expected = draftPseudonymizedMessageBuilder().build();
    final var message = draftMessageBuilder().build();

    testabilityUtil.toggleTemporaryFailure(2);

    jmsUtil.publishMessage(message);

    final var actual = testabilityUtil.awaitProcessed(
        message.getMessageId(),
        Duration.ofSeconds(5)
    );

    assertEquals(expected, actual);
  }

  @Test
  void shallMoveToDlqAfterMaximumRedeliveries() {
    final var message = draftMessageBuilder().build();

    testabilityUtil.togglePermanentFailure(true);

    jmsUtil.publishMessage(message);

    final var actual = jmsUtil.awaitProcessedToDlq(message.getMessageId(), Duration.ofSeconds(5));

    assertTrue(actual, "DLQ should contain the message");
  }

  @Test
  void shallMoveToDlqIfMessageCannotBeParsed() {
    final var message = draftMessageBuilder().build();

    jmsUtil.publishUnparsableMessage(message);

    final var actual = jmsUtil.awaitProcessedToDlq(message.getMessageId(), Duration.ofSeconds(5));

    assertTrue(actual, "DLQ should contain the message");
  }
}