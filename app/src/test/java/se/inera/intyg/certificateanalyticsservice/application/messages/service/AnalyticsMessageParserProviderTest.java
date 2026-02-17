package se.inera.intyg.certificateanalyticsservice.application.messages.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.certificateanalyticsservice.application.messages.model.AnalyticsMessageParser;

@ExtendWith(MockitoExtension.class)
class AnalyticsMessageParserProviderTest {

  @Mock
  private AnalyticsMessageParser analyticsMessageParser;

  private AnalyticsMessageParserProvider analyticsMessageParserProvider;

  @BeforeEach
  void setUp() {
    analyticsMessageParserProvider = new AnalyticsMessageParserProvider(
        List.of(analyticsMessageParser)
    );
  }

  @Test
  void shallReturnMatchingParserIfAvailable() {
    when(analyticsMessageParser.canParse("type", "version")).thenReturn(true);
    final var actual = analyticsMessageParserProvider.parser("type", "version");
    assertEquals(analyticsMessageParser, actual);
  }

  @Test
  void shallThrowIllegalArgumentExceptionIfNoParserAvailable() {
    when(analyticsMessageParser.canParse("type", "version")).thenReturn(false);
    assertThrows(IllegalArgumentException.class,
        () -> analyticsMessageParserProvider.parser("type", "version")
    );
  }
}