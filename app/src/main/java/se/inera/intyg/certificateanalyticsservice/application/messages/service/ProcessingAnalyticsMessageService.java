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
package se.inera.intyg.certificateanalyticsservice.application.messages.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import se.inera.intyg.certificateanalyticsservice.application.messages.repository.AnalyticsMessageRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProcessingAnalyticsMessageService implements AnalyticsMessageService {

  private final AnalyticsMessageParserProvider analyticsMessageParserProvider;
  private final AnalyticMessagePseudonymizerProvider analyticMessagePseudonymizerProvider;
  private final AnalyticsMessageRepository analyticsMessageRepository;

  @Override
  public void process(String body, String type, String schemaVersion) {
    final var message = analyticsMessageParserProvider.parser(type, schemaVersion).parse(body);
    final var pseudonymizedMessage =
        analyticMessagePseudonymizerProvider.pseudonymizer(message).pseudonymize(message);
    analyticsMessageRepository.save(pseudonymizedMessage);
    log.info("Processed, pseudonymized and stored message with id '{}'", message.getMessageId());
  }
}
