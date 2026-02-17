package se.inera.intyg.certificateanalyticsservice.application.messages.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import se.inera.intyg.certificateanalyticsservice.application.messages.model.AnalyticsMessagePseudonymizer;
import se.inera.intyg.certificateanalyticsservice.application.messages.model.CertificateAnalyticsMessage;

@Component
@RequiredArgsConstructor
public class AnalyticMessagePseudonymizerProvider {

  private final List<AnalyticsMessagePseudonymizer> analyticsMessagePseudonymizers;

  public AnalyticsMessagePseudonymizer pseudonymizer(CertificateAnalyticsMessage message) {
    return analyticsMessagePseudonymizers.stream()
        .filter(c -> c.canPseudonymize(message))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException(
                "No pseudonymizer found for message of class '%s'".formatted(message.getClass())
            )
        );
  }
}