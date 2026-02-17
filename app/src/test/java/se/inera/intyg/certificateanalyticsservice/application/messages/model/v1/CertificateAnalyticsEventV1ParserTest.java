package se.inera.intyg.certificateanalyticsservice.application.messages.model.v1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataMessages.draftMessageBuilder;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataMessages.replaceMessageBuilder;
import static se.inera.intyg.certificateanalyticsservice.testdata.TestDataMessages.toJson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.UncheckedIOException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CertificateAnalyticsEventV1ParserTest {

  @Mock
  private ObjectMapper objectMapper;

  @InjectMocks
  private CertificateAnalyticsEventV1Parser parser;

  @Test
  void shallSupportCertificateAnalyticMessageOfSchemaVersionV1() {
    final var canConvert = parser.canParse("certificate.analytics.event", "v1");
    assertTrue(canConvert, "Should support certificate.analytics.event of version v1");
  }

  @Test
  void shallNotSupportCertificateAnalyticMessageOfDifferentSchemaVersion() {
    final var actual = parser.canParse("certificate.analytics.event", "diffVersion");
    assertFalse(actual, "Should not support certificate.analytics.event of different version");
  }

  @Test
  void shallNotSupportDifferentMessageOfSchemaVersionV1() {
    final var actual = parser.canParse("diff.message", "v1");
    assertFalse(actual, "Should not support different message of version v1");
  }

  @Test
  void shallReturnParsedEvent() throws JsonProcessingException {
    final var excepted = replaceMessageBuilder().build();
    final var messageAsJson = toJson(excepted);

    when(objectMapper.readValue(messageAsJson, CertificateAnalyticsMessageV1.class))
        .thenReturn(excepted);

    final var actual = parser.parse(messageAsJson);

    assertEquals(excepted, actual);
  }

  @Test
  void shallThrowUncheckedIOExceptionIfMessageCannotBeDeserialized()
      throws JsonProcessingException {
    final var excepted = draftMessageBuilder().build();
    final var messageAsJson = toJson(excepted);

    when(objectMapper.readValue(messageAsJson, CertificateAnalyticsMessageV1.class))
        .thenThrow(JsonProcessingException.class);

    assertThrows(UncheckedIOException.class, () -> parser.parse(messageAsJson));
  }
}