/*
 * Copyright (C) 2026 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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

  @Mock private ObjectMapper objectMapper;

  @InjectMocks private CertificateAnalyticsEventV1Parser parser;

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
