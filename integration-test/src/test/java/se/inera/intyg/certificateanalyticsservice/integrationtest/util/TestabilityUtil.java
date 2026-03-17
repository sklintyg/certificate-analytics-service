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
package se.inera.intyg.certificateanalyticsservice.integrationtest.util;

import static org.awaitility.Awaitility.await;

import java.time.Duration;
import org.springframework.boot.resttestclient.TestRestTemplate;
import se.inera.intyg.certificateanalyticsservice.application.messages.model.PseudonymizedAnalyticsMessage;

public class TestabilityUtil {

  private final int port;
  private final TestRestTemplate restTemplate;

  public TestabilityUtil(TestRestTemplate restTemplate, int port) {
    this.restTemplate = restTemplate;
    this.port = port;
  }

  public PseudonymizedAnalyticsMessage awaitProcessed(String messageId, Duration timeout) {
    await()
        .atMost(timeout)
        .pollInterval(Duration.ofMillis(200))
        .until(
            () -> {
              final var resp =
                  restTemplate.getForEntity(
                      "http://localhost:%s/testability/messages/v1/%s".formatted(port, messageId),
                      PseudonymizedAnalyticsMessage.class);
              return resp.getStatusCode().is2xxSuccessful() && resp.getBody() != null;
            });

    return restTemplate
        .getForEntity(
            "http://localhost:%s/testability/messages/v1/%s".formatted(port, messageId),
            PseudonymizedAnalyticsMessage.class)
        .getBody();
  }

  public void reset() {
    restTemplate.getForEntity(
        "http://localhost:%s/testability/messages/reset".formatted(port), Void.class);
  }

  public void toggleTemporaryFailure(int numberOfFailures) {
    restTemplate.getForEntity(
        "http://localhost:%s/testability/messages/fail/temporary/%s"
            .formatted(port, numberOfFailures),
        Void.class);
  }

  public void togglePermanentFailure(boolean permanentFailure) {
    restTemplate.getForEntity(
        "http://localhost:%s/testability/messages/fail/permanent/%s"
            .formatted(port, permanentFailure),
        Void.class);
  }
}
