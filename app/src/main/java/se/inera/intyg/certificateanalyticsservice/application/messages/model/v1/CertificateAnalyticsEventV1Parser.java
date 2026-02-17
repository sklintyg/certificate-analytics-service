package se.inera.intyg.certificateanalyticsservice.application.messages.model.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.UncheckedIOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateanalyticsservice.application.messages.model.AnalyticsMessageParser;

@Component
@RequiredArgsConstructor
public class CertificateAnalyticsEventV1Parser implements AnalyticsMessageParser {

  private static final String TYPE = "certificate.analytics.event";
  private static final String SCHEMA_VERSION = "v1";

  private final ObjectMapper objectMapper;

  public boolean canParse(String type, String schemaVersion) {
    return TYPE.equals(type) && SCHEMA_VERSION.equals(schemaVersion);
  }

  public CertificateAnalyticsMessageV1 parse(String message) {
    try {
      return objectMapper.readValue(message, CertificateAnalyticsMessageV1.class);
    } catch (JsonProcessingException e) {
      throw new UncheckedIOException(e);
    }
  }
}
