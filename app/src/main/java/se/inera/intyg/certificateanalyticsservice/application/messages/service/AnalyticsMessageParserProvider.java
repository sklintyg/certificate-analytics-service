package se.inera.intyg.certificateanalyticsservice.application.messages.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateanalyticsservice.application.messages.model.AnalyticsMessageParser;

@Component
@RequiredArgsConstructor
public class AnalyticsMessageParserProvider {

  private final List<AnalyticsMessageParser> analyticsMessageParsers;

  public AnalyticsMessageParser parser(String type, String schemaVersion) {
    return analyticsMessageParsers.stream()
        .filter(c -> c.canParse(type, schemaVersion))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException(
                "No parser found for type '%s' and schema version '%s'".formatted(type, schemaVersion)
            )
        );
  }
}
