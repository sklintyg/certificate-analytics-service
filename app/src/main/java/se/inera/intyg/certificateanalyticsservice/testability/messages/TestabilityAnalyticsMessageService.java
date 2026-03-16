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
    super(
        analyticsMessageParserProvider,
        analyticsMessagePseudonymizerProvider,
        analyticMessageRepository);
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
