package se.inera.intyg.certificateanalyticsservice.testability.messages;

import static se.inera.intyg.certificateanalyticsservice.testability.configuration.TestabilityConfiguration.TESTABILITY_PROFILE;

import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import se.inera.intyg.certificateanalyticsservice.application.messages.repository.AnalyticsMessageRepository;
import se.inera.intyg.certificateanalyticsservice.application.messages.service.AnalyticMessagePseudonymizerProvider;
import se.inera.intyg.certificateanalyticsservice.application.messages.service.AnalyticsMessageParserProvider;
import se.inera.intyg.certificateanalyticsservice.application.messages.service.ProcessingAnalyticsMessageService;

@Primary
@Service
@Profile(TESTABILITY_PROFILE)
public class TestabilityAnalyticsMessageService extends ProcessingAnalyticsMessageService {

  private final AtomicInteger temporaryFailsLeft = new AtomicInteger(0);
  private volatile boolean permanentFailure = false;

  public TestabilityAnalyticsMessageService(
      AnalyticsMessageParserProvider analyticsMessageParserProvider,
      AnalyticMessagePseudonymizerProvider analyticsMessagePseudonymizerProvider,
      AnalyticsMessageRepository analyticMessageRepository) {
    super(analyticsMessageParserProvider,
        analyticsMessagePseudonymizerProvider,
        analyticMessageRepository
    );
  }

  @Override
  public void process(String body, String type, String schemaVersion) {
    maybeThrowPermanentFailure();
    maybeThrowTemporaryFailure();

    super.process(body, type, schemaVersion);
  }

  public void toggleTemporaryFailure(int numberOfTimes) {
    temporaryFailsLeft.set(numberOfTimes);
  }

  public void togglePermanentFailure(boolean permanentFailure) {
    this.permanentFailure = permanentFailure;
  }

  public void reset() {
    temporaryFailsLeft.set(0);
    permanentFailure = false;
  }

  private void maybeThrowPermanentFailure() {
    if (permanentFailure) {
      throw new IllegalArgumentException("Simulated permanent failure");
    }
  }

  private void maybeThrowTemporaryFailure() {
    if (temporaryFailsLeft.getAndDecrement() > 0) {
      throw new IllegalStateException("Simulated temporary failure");
    }
  }
}