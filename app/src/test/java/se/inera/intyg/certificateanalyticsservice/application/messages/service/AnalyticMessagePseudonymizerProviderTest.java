package se.inera.intyg.certificateanalyticsservice.application.messages.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataMessages.draftMessageBuilder;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateanalyticsservice.application.messages.model.AnalyticsMessagePseudonymizer;

@ExtendWith(MockitoExtension.class)
class AnalyticMessagePseudonymizerProviderTest {

  @Mock
  private AnalyticsMessagePseudonymizer analyticsMessagePseudonymizer;

  private AnalyticMessagePseudonymizerProvider analyticMessagePseudonymizerProvider;

  @BeforeEach
  void setUp() {
    analyticMessagePseudonymizerProvider = new AnalyticMessagePseudonymizerProvider(
        List.of(analyticsMessagePseudonymizer)
    );
  }

  @Test
  void shallReturnMatchingParserIfAvailable() {
    final var message = draftMessageBuilder().build();
    when(analyticsMessagePseudonymizer.canPseudonymize(message)).thenReturn(true);
    final var actual = analyticMessagePseudonymizerProvider.pseudonymizer(message);
    assertEquals(analyticsMessagePseudonymizer, actual);
  }

  @Test
  void shallThrowIllegalArgumentExceptionIfNoParserAvailable() {
    final var message = draftMessageBuilder().build();
    when(analyticsMessagePseudonymizer.canPseudonymize(message)).thenReturn(false);
    assertThrows(IllegalArgumentException.class,
        () -> analyticMessagePseudonymizerProvider.pseudonymizer(message)
    );
  }
}