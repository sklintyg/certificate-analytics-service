package se.inera.intyg.certificateanalyticsservice.application.messages.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataMessages.draftMessageBuilder;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataMessages.toJson;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataPseudonymized.draftPseudonymizedMessageBuilder;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateanalyticsservice.application.messages.model.AnalyticsMessageParser;
import se.inera.intyg.certificateanalyticsservice.application.messages.model.AnalyticsMessagePseudonymizer;
import se.inera.intyg.certificateanalyticsservice.application.messages.repository.AnalyticsMessageRepository;

@ExtendWith(MockitoExtension.class)
class ProcessingAnalyticsMessageServiceTest {

  @Mock
  private AnalyticsMessageParser analyticsMessageParser;
  @Mock
  private AnalyticsMessageParserProvider analyticsMessageParserProvider;
  @Mock
  private AnalyticsMessageRepository analyticsMessageRepository;
  @Mock
  private AnalyticsMessagePseudonymizer analyticsMessagePseudonymizer;
  @Mock
  private AnalyticMessagePseudonymizerProvider analyticMessagePseudonymizerProvider;
  @InjectMocks
  private ProcessingAnalyticsMessageService processingAnalyticsMessageService;

  @Test
  void shallPersistProcessedMessage() {
    final var message = draftMessageBuilder().build();
    final var messageAsJson = toJson(message);
    final var pseudonymizedMessage = draftPseudonymizedMessageBuilder().build();

    when(analyticsMessageParserProvider.parser(message.getType(),
        message.getSchemaVersion())).thenReturn(analyticsMessageParser);
    when(analyticsMessageParser.parse(messageAsJson)).thenReturn(message);
    when(analyticMessagePseudonymizerProvider.pseudonymizer(message))
        .thenReturn(analyticsMessagePseudonymizer);
    when(analyticsMessagePseudonymizer.pseudonymize(message)).thenReturn(pseudonymizedMessage);

    processingAnalyticsMessageService.process(
        messageAsJson,
        message.getType(),
        message.getSchemaVersion()
    );

    verify(analyticsMessageRepository).save(pseudonymizedMessage);
  }
}