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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.inera.intyg.certificateanalyticsservice.application.messages.model.PseudonymizedAnalyticsMessage;
import se.inera.intyg.certificateanalyticsservice.application.messages.repository.AnalyticsMessageRepository;
import se.inera.intyg.certificateanalyticsservice.infrastructure.pseudonymization.PseudonymizationTokenGenerator;

@Slf4j
@Profile(TESTABILITY_PROFILE)
@RestController
@RequestMapping("/testability")
@RequiredArgsConstructor
public class TestabilityMessageController {

  private final TestabilityAnalyticsMessageService testabilityAnalyticsMessageService;
  private final AnalyticsMessageRepository analyticsMessageRepository;
  private final PseudonymizationTokenGenerator pseudonymizationTokenGenerator;

  @GetMapping("/messages/v1/{messageId}")
  public PseudonymizedAnalyticsMessage getMessage(@PathVariable String messageId) {
    final var pseudonymizedMessageId = pseudonymizationTokenGenerator.id(messageId);
    log.info(
        "Testability get message with id '{}' that is pseudonymized to '{}'",
        messageId,
        pseudonymizedMessageId);
    return analyticsMessageRepository.findByMessageId(pseudonymizedMessageId);
  }

  @GetMapping("/messages/reset")
  public void reset() {
    log.info("Testability reset");
    analyticsMessageRepository.clear();
    testabilityAnalyticsMessageService.reset();
  }

  @GetMapping("/messages/fail/temporary/{numberOfTimes}")
  public void failNext(@PathVariable int numberOfTimes) {
    log.info("Testability temporary failure for next {} calls", numberOfTimes);
    testabilityAnalyticsMessageService.toggleTemporaryFailure(numberOfTimes);
  }

  @GetMapping("/messages/fail/permanent/{permanentFailure}")
  public void permanent(@PathVariable boolean permanentFailure) {
    log.info("Testability permanent failure set to {}", permanentFailure);
    testabilityAnalyticsMessageService.togglePermanentFailure(permanentFailure);
  }
}
